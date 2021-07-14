package sample;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class CLientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    /** 响应 */
    private TextArea answer;

    public CLientHandler(TextArea answer) {
        this.answer = answer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        String response=datagramPacket.content().toString(CharsetUtil.UTF_8);
        System.out.println(response);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                answer.setText(response);
            }
        });
        channelHandlerContext.close();
    }
}
