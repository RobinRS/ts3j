
package com.github.manevolent.ts3j.protocol.packet.statistics;

import com.github.manevolent.ts3j.protocol.Packet;

import java.util.LinkedList;
import java.util.List;

public class PacketStatistics {
    private final List<DataPoint> sentPacketList = new LinkedList<>();
    private final List<DataPoint> receivedPacketList = new LinkedList<>();

    private int sentBytes;
    private int sentPackets;
    private int receivedBytes;
    private int receivedPackets;

    public void processIncoming(Packet packet) {
        int size = packet.getSize();

        receivedBytes += size;
        receivedPackets ++;

        receivedPacketList.add(new DataPoint(packet.getCreatedNanotime() / 1000L, packet.getSize()));

        long t = System.currentTimeMillis() - 60_000L;
        receivedPacketList.removeIf(x -> x.getTime() < t);
    }

    public void processOutgoing(Packet packet) {
        int size = packet.getSize();

        sentBytes += size;
        sentPackets ++;

        sentPacketList.add(new DataPoint(packet.getCreatedNanotime() / 1000L, packet.getSize()));

        // Remove oldest
        long t = System.currentTimeMillis() - 60_000L;
        sentPacketList.removeIf(x -> x.getTime() < t);
    }

    public int getSentPackets() {
        return sentPackets;
    }

    public int getSentBytes() {
        return sentBytes;
    }

    public int getSentPacketsLastSecond() {
        long now = System.currentTimeMillis() - 1_000L;

        return (int) sentPacketList.stream()
                .filter(x -> x.getTime() >= now)
                .count();
    }

    public int getSentPacketsLastMinute() {
        long now = System.currentTimeMillis() - 60_000L;

        return (int) sentPacketList.stream()
                .filter(x -> x.getTime() >= now)
                .count();
    }

    public int getReceivedPackets() {
        return receivedPackets;
    }

    public int getReceivedPacketsLastSecond() {
        long now = System.currentTimeMillis() - 1_000L;

        return (int) receivedPacketList.stream()
                .filter(x -> x.getTime() >= now)
                .count();
    }

    public int getReceivedPacketsLastMinute() {
        long now = System.currentTimeMillis() - 60_000L;

        return (int) receivedPacketList.stream()
                .filter(x -> x.getTime() >= now)
                .count();
    }


    public int getReceivedBytes() {
        return receivedBytes;
    }

    public int getSentBytesLastMinute() {
        long now = System.currentTimeMillis() - 60_000L;

        return sentPacketList.stream()
                .filter(x -> x.getTime() >= now)
                .map(DataPoint::getSize)
                .reduce((a, b) -> a + b)
                .orElse(0);
    }

    public int getSentBytesLastSecond() {
        long now = System.currentTimeMillis() - 1_000L;

        return sentPacketList.stream()
                .filter(x -> x.getTime() >= now)
                .map(DataPoint::getSize)
                .reduce((a, b) -> a + b)
                .orElse(0);
    }

    public int getReceivedBytesLastMinute() {
        long now = System.currentTimeMillis() - 60_000L;

        return receivedPacketList.stream()
                .filter(x -> x.getTime() >= now)
                .map(DataPoint::getSize)
                .reduce((a, b) -> a + b)
                .orElse(0);
    }

    public int getReceivedBytesLastSecond() {
        long now = System.currentTimeMillis() - 1_000L;

        return receivedPacketList.stream()
                .filter(x -> x.getTime() >= now)
                .map(DataPoint::getSize)
                .reduce((a, b) -> a + b)
                .orElse(0);
    }

    private final class DataPoint {
        private final long time;
        private final int size;

        private DataPoint(long time, int size) {
            this.time = time;
            this.size = size;
        }

        public long getTime() {
            return time;
        }

        public int getSize() {
            return size;
        }
    }
}