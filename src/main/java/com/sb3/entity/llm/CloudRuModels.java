package com.sb3.entity.llm;

import lombok.Getter;

@Getter
public enum CloudRuModels {
    CLOUD_GIGA_CHAT_2("GigaChat/GigaChat-2-Max");

    private String title;

    private CloudRuModels(String title) {
        this.title = title;
    }
}
