package sample;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.InetSocketAddress;

public class NettyRunner implements Runnable{
    /** 端口 */
    public TextField port;
    /** 地址 */
    public TextField ip;
    /** 内容 */
    public TextArea content;
    /** 响应 */
    public TextArea answer;

    public NettyRunner(TextField port, TextField ip, TextArea content, TextArea answer) {
        this.port = port;
        this.ip = ip;
        this.content = content;
        this.answer = answer;
    }

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b=new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new CLientHandler(answer));
            Channel ch=b.bind(0).sync().channel();
            //向网段内的所有机器广播
            ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                    content.getText(),CharsetUtil.UTF_8), new InetSocketAddress(ip.getText(),Integer.parseInt(port.getText())))).sync();
            //客户端等待15s用于接收服务端的应答消息，然后退出并释放资源
            if(!ch.closeFuture().await(10000)){
                System.out.println("查询超时！");
                answer.setText("查询超时！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            answer.setText(e.getMessage());
        } finally {
            group.shutdownGracefully();
        }
    }
}
