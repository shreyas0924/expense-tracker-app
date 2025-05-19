import { MessagesUtil } from "../utils/messageUtils";
import { LLMService } from "../service/llm";

export class MessageService {
    private messageUtil: MessagesUtil;
    private llmService: LLMService;

    constructor() {
        this.messageUtil = new MessagesUtil();
        this.llmService = new LLMService();
    }

    async processMessage(message: string) {
        if (this.messageUtil.isBankSms(message)) {
            return await this.llmService.runLLM(message);
        }
        return null;
    }
}