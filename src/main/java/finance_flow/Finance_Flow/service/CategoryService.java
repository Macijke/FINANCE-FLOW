package finance_flow.Finance_Flow.service;

import finance_flow.Finance_Flow.dto.request.CategoryRequest;
import finance_flow.Finance_Flow.dto.response.CategoryResponse;
import finance_flow.Finance_Flow.model.enums.TransactionType;
import finance_flow.Finance_Flow.security.UserPrincipal;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void deleteCategory(Long id);

    CategoryResponse getCategoryById(Long id);

    List<CategoryResponse> getAllCategories();

    List<CategoryResponse> getCategoriesByType(TransactionType type);

    void initializeDefaultCategories(UserPrincipal userPrincipal);

}
