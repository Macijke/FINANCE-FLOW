CREATE TABLE budgets (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_id BIGINT,
    limit_amount DECIMAL(10, 2) NOT NULL,
    month DATE NOT NULL,
    alert_enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL,
    UNIQUE (user_id, category_id, month)
);

CREATE INDEX idx_budgets_user_month ON budgets(user_id, month);
CREATE INDEX idx_budgets_category ON budgets(category_id);
