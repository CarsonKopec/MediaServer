package dev.imagineforgee.ms.server;

public final class PlaybackState {
    private boolean playing;
    private double position;
    private double duration;
    private int volume;
    private double speed;

    public PlaybackState() {
        this.playing = false;
        this.position = 0;
        this.duration = 0;
        this.volume = 100;
        this.speed = 1.0;
    }

    // Getters and setters
    public boolean isPlaying() { return playing; }
    public void setPlaying(boolean playing) { this.playing = playing; }

    public double getPosition() { return position; }
    public void setPosition(double position) { this.position = position; }

    public double getDuration() { return duration; }
    public void setDuration(double duration) { this.duration = duration; }

    public int getVolume() { return volume; }
    public void setVolume(int volume) { this.volume = volume; }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
}
