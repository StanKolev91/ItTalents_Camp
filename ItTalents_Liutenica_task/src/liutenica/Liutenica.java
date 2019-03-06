package liutenica;

import java.util.Date;
import java.util.Objects;

public class Liutenica {
    private int kilos;
    private Date date;

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Liutenica liutenica = (Liutenica) o;
        return kilos == liutenica.kilos &&
                Objects.equals(date, liutenica.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kilos, date);
    }

    public int getKilos() {
        return kilos;
    }

    public Liutenica() {
        this.kilos = LiutenicaDemo.random(3, 12);
        this.date = new Date();
    }
}
