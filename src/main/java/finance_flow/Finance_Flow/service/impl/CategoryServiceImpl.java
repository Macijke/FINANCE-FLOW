package finance_flow.Finance_Flow.service.impl;

import finance_flow.Finance_Flow.dto.request.CategoryRequest;
import finance_flow.Finance_Flow.dto.response.CategoryResponse;
import finance_flow.Finance_Flow.exception.BadRequestException;
import finance_flow.Finance_Flow.exception.ResourceNotFoundException;
import finance_flow.Finance_Flow.model.Category;
import finance_flow.Finance_Flow.model.User;
import finance_flow.Finance_Flow.model.enums.TransactionType;
import finance_flow.Finance_Flow.repository.CategoryRepository;
import finance_flow.Finance_Flow.service.CategoryService;
import finance_flow.Finance_Flow.util.DefaultCategoriesGenerator;
import finance_flow.Finance_Flow.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();
        boolean exists = categoryRepository.existsByUserAndNameAndType(
                currentUser, request.getName(), request.getType()
        );
        if (exists) {
            log.warn("Category with name '{}' and type '{}' already exists for user '{}'",
                    request.getName(), request.getType(), currentUser.getEmail());
            throw new BadRequestException("Category with the same name and type already exists.");
        }

        Category category = Category.builder()
                .user(currentUser)
                .name(request.getName())
                .type(request.getType())
                .color(request.getColor())
                .icon(request.getIcon())
                .description(request.getDescription())
                .isDefault(false)
                .isActive(true)
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();

        Category saved = categoryRepository.save(category);

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        log.info("Updating category with id: {}", id);

        User currentUser = SecurityUtils.getCurrentUser();

        Category category = categoryRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!category.getName().equals(request.getName()) ||
                !category.getType().equals(request.getType())) {

            boolean exists = categoryRepository.existsByUserAndNameAndType(
                    currentUser,
                    request.getName(),
                    request.getType()
            );

            if (exists) {
                throw new BadRequestException("Category with the same name and type already exists");
            }
        }

        category.setName(request.getName());
        category.setType(request.getType());
        category.setColor(request.getColor());
        category.setIcon(request.getIcon());
        category.setDescription(request.getDescription());
        category.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        Category updated = categoryRepository.save(category);

        log.info("Category updated successfully");

        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        User currentUser = SecurityUtils.getCurrentUser();
        Category category = categoryRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new BadRequestException("Category not found"));
        categoryRepository.delete(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        User currentUser = SecurityUtils.getCurrentUser();
        Category category = categoryRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new BadRequestException("Category not found"));

        return mapToResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        User currentUser = SecurityUtils.getCurrentUser();
        List<Category> categories = categoryRepository.findByUser(currentUser);

        return categories.stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoriesByType(TransactionType type) {
        User currentUser = SecurityUtils.getCurrentUser();
        List<Category> categories = categoryRepository.findByUserAndType(currentUser, type);
        return categories.stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional
    public void initializeDefaultCategories() {
        User currentUser = SecurityUtils.getCurrentUser();

        if (categoryRepository.countByUser(currentUser) > 0) {
            log.debug("Categories already initialized for user {}", currentUser.getId());
            return;
        }

        log.info("Initializing default categories for user {}", currentUser.getId());

        List<Category> defaultCategories = DefaultCategoriesGenerator.createDefaultCategories(currentUser);
        categoryRepository.saveAll(defaultCategories);

        log.info("Default categories initialized successfully");
    }

    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .color(category.getColor())
                .icon(category.getIcon())
                .description(category.getDescription())
                .isDefault(category.getIsDefault())
                .isActive(category.getIsActive())
                .displayOrder(category.getDisplayOrder())
                .createdAt(category.getCreatedAt())
                .build();
    }
}
