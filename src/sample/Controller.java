package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {

    /** 端口 */
    public TextField port;
    /** 地址 */
    public TextField ip;
    /** 内容 */
    public TextArea content;
    /** 响应 */
    public TextArea answer;

    /**
     * 发送方法
     */
    @FXML
    private void send(){
        Thread thread = new Thread(new NettyRunner(port,ip,content,answer));
        thread.start();
    }
}
