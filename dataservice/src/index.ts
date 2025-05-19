import express, { Request, Response, Application } from "express";
import { Kafka } from "kafkajs";
import { MessageService } from "./service/messageService";
import { config } from "dotenv";

config();

const app: Application = express();
app.use(express.json());

const messageService = new MessageService();
const kafkaHost = process.env.KAFKA_HOST || "localhost";
const kafkaPort = process.env.KAFKA_PORT || "9092";
const kafkaBootstrapServers = `${kafkaHost}:${kafkaPort}`;
console.log(`Kafka server is ${kafkaBootstrapServers}\n`);

const kafka = new Kafka({ brokers: [kafkaBootstrapServers] });
const producer = kafka.producer();

(async () => {
  await producer.connect();
})();

app.post(
  "/v1/ds/message",
  async (req: Request, res: Response): Promise<any> => {
    const userId = req.headers["x-user-id"] as string;
    if (!userId) {
      return res.status(400).json({ error: "x-user-id header is required" });
    }

    const message = req.body.message;
    const result = await messageService.processMessage(message);

    if (result) {
      const serializedResult = { ...result, user_id: userId };
      await producer.send({
        topic: process.env.KAFKA_TOPIC_NAME!,
        messages: [{ value: JSON.stringify(serializedResult) }],
      });
      return res.json(serializedResult);
    } else {
      return res.status(400).json({ error: "Invalid message format" });
    }
  }
);

app.get("/", (req: Request, res: Response) => {
  res.send("Hello world");
});

app.get("/health", (req: Request, res: Response) => {
  res.send("OK");
});

const PORT = process.env.PORT || 8010;
app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});
