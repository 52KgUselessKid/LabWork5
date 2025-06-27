package Net;

import java.io.Serializable;

public class Answer implements Serializable {
    String content;

    public Answer(String content)
    {
        this.content = content;
    }

    String getContent()
    {
        return content;
    }
}
