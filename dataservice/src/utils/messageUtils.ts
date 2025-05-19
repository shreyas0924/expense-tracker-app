export class MessagesUtil {
    private wordsToSearch: string[] = ["spent", "bank", "card"];

    isBankSms(message: string): boolean {
        const pattern = new RegExp(`\\b(?:${this.wordsToSearch.map(word => word.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')).join('|')})\\b`, 'i');
        return pattern.test(message);
    }
}
