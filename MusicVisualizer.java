import processing.core.PApplet;
import ddf.minim.*;
import ddf.minim.analysis.FFT;

public class MusicVisualizer extends PApplet {
    Minim minim;
    AudioPlayer audio;
    FFT fft;

    int xCenter;
    int yCenter;

    public static void main(String[] args) {
        PApplet.main("MusicVisualizer");
    }

    public void settings() {
        size(1000, 500);
    }

    public void setup() {
        xCenter = width / 2;
        yCenter = height / 2;

        minim = new Minim(this);
        audio = minim.loadFile("res/piano.mp3");

        fft = new FFT(audio.bufferSize(), audio.sampleRate());

        background(255, 255, 255);

        audio.loop();

    }

    public void draw() {
        background(0, 0, 0);
        float[] leftChannel = audio.left.toArray();
        float[] rightChannel = audio.right.toArray();

        fft.forward(audio.mix);

        for (int i = 0; i < leftChannel.length - 1; i++) {
            drawChannel(leftChannel, i, 1);
            drawChannel(rightChannel, i, -1);
        }

        for (int i = 0; i < fft.specSize(); i++) {
            drawFrequency(i);
        }
    }

    public void drawChannel(float[] channel, int index, int direction) {
        strokeWeight(2);
        stroke(0, 0, 255);
        line(index,
                yCenter + direction * abs(channel[index]) * 300,
                index + 1,
                yCenter + direction * abs(channel[index + 1]) * 300);
    }

    public void drawFrequency(int index) {
        //strokeWeight(1);
        //stroke(255,255,255);
        noStroke();

        for (int i = 1; i <= 3; i++) {
            fill(255, 255, 255, 100 / sq(i));
            circle(xCenter, yCenter, fft.getBand(index) * 3 * sq(i));
        }
    }

}