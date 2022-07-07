package com.example.websocketdemo.entity;

import lombok.Data;

@Data
public class IMessage {
    String  from;
    String  to;
    String message;
}
