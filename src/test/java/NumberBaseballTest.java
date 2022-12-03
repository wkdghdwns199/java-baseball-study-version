import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.event.CaretListener;

import static org.assertj.core.api.Assertions.*;

public class NumberBaseballTest {
    @Test
    @DisplayName("입력의 길이가 3인지")
    void test1() {
        String string = "13";
        Answer answer = new Answer(string);
        assertThat(answer.toString());
    }
}
