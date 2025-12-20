-- Debt (負債主體)
CREATE TABLE debts (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    total_amount INTEGER NOT NULL CHECK (total_amount > 0),
    installment_count INTEGER NOT NULL CHECK (installment_count > 0),
    start_date DATE NOT NULL,
    interest_rate DECIMAL(5, 2) NULL CHECK (interest_rate >= 0),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'PAID_OFF')),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- DebtInstallment (分期明細)
CREATE TABLE debt_installments (
    id BIGSERIAL PRIMARY KEY,
    debt_id BIGINT NOT NULL REFERENCES debts(id) ON DELETE CASCADE,
    installment_number INTEGER NOT NULL CHECK (installment_number > 0),
    amount INTEGER NOT NULL CHECK (amount > 0),
    due_date DATE NOT NULL,
    paid BOOLEAN NOT NULL DEFAULT FALSE,
    paid_at DATE NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(debt_id, installment_number)
);

-- Indexes for performance
CREATE INDEX idx_debts_status ON debts(status);
CREATE INDEX idx_installments_debt_id ON debt_installments(debt_id);
CREATE INDEX idx_installments_due_date ON debt_installments(due_date);
CREATE INDEX idx_installments_paid ON debt_installments(paid);
CREATE INDEX idx_installments_debt_paid ON debt_installments(debt_id, paid);


