package org.lonelyproject.backend.dto;

import java.io.Serializable;

public class PromptDto implements Serializable {

    private int promptId;
    private String promptName;
    private String text;

    public PromptDto() {
    }

    public PromptDto(String text, int promptId, String promptName) {
        this.text = text;
        this.promptId = promptId;
        this.promptName = promptName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPromptId() {
        return promptId;
    }

    public void setPromptId(int promptId) {
        this.promptId = promptId;
    }

    public String getPromptName() {
        return promptName;
    }

    public void setPromptName(String promptName) {
        this.promptName = promptName;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
            "text = " + text + ", " +
            "promptId = " + promptId + ", " +
            "promptName = " + promptName + ")";
    }
}
