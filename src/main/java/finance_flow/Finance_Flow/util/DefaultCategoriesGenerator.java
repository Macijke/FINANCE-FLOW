package finance_flow.Finance_Flow.util;

import finance_flow.Finance_Flow.model.Category;
import finance_flow.Finance_Flow.model.User;
import finance_flow.Finance_Flow.model.enums.TransactionType;

import java.util.Arrays;
import java.util.List;

public class DefaultCategoriesGenerator {

    public static List<Category> createDefaultCategories(User user) {
        return Arrays.asList(
                createCategory(user, "Food", TransactionType.EXPENSE, "#EF4444", "🍔", "Meals and groceries", 1),
                createCategory(user, "Transport", TransactionType.EXPENSE, "#F59E0B", "🚗", "Gas, public transport, taxi", 2),
                createCategory(user, "Entertainment", TransactionType.EXPENSE, "#8B5CF6", "🎬", "Movies, games, hobbies", 3),
                createCategory(user, "Shopping", TransactionType.EXPENSE, "#EC4899", "🛍️", "Clothes, accessories", 4),
                createCategory(user, "Utilities", TransactionType.EXPENSE, "#06B6D4", "💡", "Electricity, water, internet", 5),
                createCategory(user, "Healthcare", TransactionType.EXPENSE, "#10B981", "⚕️", "Medicines, doctor visits", 6),
                createCategory(user, "Education", TransactionType.EXPENSE, "#3B82F6", "📚", "Courses, books, training", 7),
                createCategory(user, "Other", TransactionType.EXPENSE, "#6B7280", "📌", "Miscellaneous expenses", 8),

                createCategory(user, "Salary", TransactionType.INCOME, "#10B981", "💰", "Monthly salary", 1),
                createCategory(user, "Freelance", TransactionType.INCOME, "#059669", "💻", "Freelance work", 2),
                createCategory(user, "Investment", TransactionType.INCOME, "#047857", "📈", "Dividends, interest", 3),
                createCategory(user, "Bonus", TransactionType.INCOME, "#34D399", "🎁", "Bonuses, gifts", 4),
                createCategory(user, "Other Income", TransactionType.INCOME, "#6EE7B7", "➕", "Other income sources", 5)
        );
    }

    private static Category createCategory(
            User user,
            String name,
            TransactionType type,
            String color,
            String icon,
            String description,
            Integer displayOrder
    ) {
        return Category.builder()
                .user(user)
                .name(name)
                .type(type)
                .color(color)
                .icon(icon)
                .description(description)
                .isDefault(true)
                .isActive(true)
                .displayOrder(displayOrder)
                .build();
    }
}
