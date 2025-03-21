# Stage 1: Build the frontend
FROM node:18-alpine AS builder
WORKDIR /app
# Copy package.json and package-lock.json
COPY package*.json ./
# Install dependencies
RUN npm install
# Copy the rest of the application code
COPY . .
# Expose the port the app runs on
EXPOSE 5173
# Command to run the app
CMD ["npm", "run", "dev"]