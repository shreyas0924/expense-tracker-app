import { config } from "dotenv";
import { Expense, ExpenseSchema } from "./expense";
import { ChatMistralAI } from "@langchain/mistralai";
import { ChatPromptTemplate } from "@langchain/core/prompts";

config();

export class LLMService {
  private prompt: ChatPromptTemplate;
  private llm: ChatMistralAI;
  private runnable: any;

  constructor() {
    this.prompt = ChatPromptTemplate.fromMessages([
      {
        role: "system",
        content: `You are an expert data extraction algorithm specialized in structured information retrieval. 
        Your task is to extract only the relevant attributes from the text while preserving accuracy.
        - Always extract the requested attributes, even if the value is missing or unclear.
        - If a value is not explicitly mentioned, infer it when possible. Otherwise, return 'unknown' instead of null.
        - Maintain consistent data formatting (e.g., numbers as strings, currency codes as standard ISO 4217 codes like 'USD', 'INR').
        - Do not add extra information beyond what is given in the text.
        
        Extracted data must be in a structured JSON format.`,
      },

      { role: "human", content: "{text}" },
    ]);

    const apiKey = process.env.MISTRAL_API_KEY;
    this.llm = new ChatMistralAI({
      apiKey,
      model: "mistral-large-latest",
      temperature: 0,
    });

    this.runnable = this.prompt.pipe(
      this.llm.withStructuredOutput(ExpenseSchema)
    );
  }

  async runLLM(message: string): Promise<Expense> {
    return await this.runnable.invoke({ text: message });
  }
}
