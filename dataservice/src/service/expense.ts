import { z } from 'zod';

export const ExpenseSchema = z.object({
    amount: z.string().optional().describe("Expense made on the transaction").nullable(),
    merchant: z.string().optional().describe("Merchant name whom the transaction has been made").nullable(),
    currency: z.string().optional().describe("Currency of the transaction").nullable(),
});

export type Expense = z.infer<typeof ExpenseSchema>;

export class ExpenseClass {
    amount?: string;
    merchant?: string;
    currency?: string;

    constructor(data: Partial<Expense>) {
        this.amount = data.amount ?? "Null";
        this.merchant = data.merchant ?? "No Merchant Provided";
        this.currency = data.currency ?? "No Currency Mentioned";
    }

    serialize() {
        return {
            amount: this.amount,
            merchant: this.merchant,
            currency: this.currency,
        };
    }
}
