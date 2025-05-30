package Itay.network;


import Itay.model.Move;

import java.io.Serializable;

public class Message implements Serializable {
    public enum Type {
        MOVE,
        CHAT,
        START,
        END,
        ACK,
        ERROR
    }

    private final Type type;
    private final Move move;
    private final String chat;
    private final String systemMessage;

    private final int senderId;       // Required
    private final Integer targetId;   // Optional

    public Message(Type type, int senderId, Integer targetId, Move move, String chat, String systemMessage) {
        this.type = type;
        this.senderId = senderId;
        this.targetId = targetId;
        this.move = move;
        this.chat = chat;
        this.systemMessage = systemMessage;
    }

    // Convenience constructors:
    public static Message move(int senderId, Move move) {
        return new Message(Type.MOVE, senderId, null, move, null, null);
    }

    public static Message chat(int senderId, String chat) {
        return new Message(Type.CHAT, senderId, null, null, chat, null);
    }

    public static Message system(Type type, int senderId, Integer targetId, String msg) {
        return new Message(type, senderId, targetId, null, null, msg);
    }

    // Getters
    public Type getType() { return type; }
    public Move getMove() { return move; }
    public String getChat() { return chat; }
    public String getSystemMessage() { return systemMessage; }
    public int getSenderId() { return senderId; }
    public Integer getTargetId() { return targetId; }
}

