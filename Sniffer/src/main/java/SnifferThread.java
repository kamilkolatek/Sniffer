import org.pcap4j.core.*;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;

import java.io.IOException;
import java.net.InetAddress;

public class SnifferThread extends Thread {
    private final SnifferController snifferController;
    private final String deviceAddress;
    private PcapHandle handle;

    public SnifferThread(SnifferController snifferController, String deviceAddress) {
        this.snifferController = snifferController;
        this.deviceAddress = deviceAddress;
    }

    PacketListener listener = new PacketListener() {
        @Override
        public void gotPacket(Packet packet) {
            IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
            TcpPacket tcpPacket = packet.get(TcpPacket.class);

            if (ipV4Packet != null && tcpPacket != null) {
                String sourceIP = (ipV4Packet.getHeader().getSrcAddr()).toString();
                String destinationIP = (ipV4Packet.getHeader().getDstAddr()).toString();
                Integer packetSize = packet.length();
                snifferController.addPacket(sourceIP, destinationIP, packetSize, tcpPacket.getHeader().toString());
            }
        }
    };

    @Override
    public void run() {
        try {
            PcapNetworkInterface device = Pcaps.getDevByAddress(InetAddress.getByName(deviceAddress));
            System.out.println("Your device: " + device);
            int snapshotLength = 65536; // in bytes
            int readTimeout = 1000; // in milliseconds
            handle = device.openLive(snapshotLength, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, readTimeout);
            handle.loop(0, listener);
            handle.close();
        } catch (IOException | PcapNativeException | InterruptedException | NotOpenException e) {
            e.printStackTrace();
        }
    }

    public PcapHandle getHandle() {
        return handle;
    }
}
