package System;

public class TimeSpan {
    private final long ticks;

    private static final long TICKS_PER_MILLISECOND = 10_000L;
    private static final long TICKS_PER_SECOND = 10_000_000L;

    public TimeSpan(long ticks) {
        this.ticks = ticks;
    }

    public static TimeSpan FromMilliseconds(double ms) {
        return new TimeSpan((long)(ms * TICKS_PER_MILLISECOND));
    }

    public static TimeSpan FromSeconds(double seconds) {
        return new TimeSpan((long)(seconds * TICKS_PER_SECOND));
    }

    public double TotalMilliseconds() {
        return (double) ticks / TICKS_PER_MILLISECOND;
    }

    public double TotalSeconds() {
        return (double) ticks / TICKS_PER_SECOND;
    }

    public long Ticks() {
        return ticks;
    }
}
