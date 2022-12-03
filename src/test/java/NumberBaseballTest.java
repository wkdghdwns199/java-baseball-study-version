import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.event.CaretListener;

import static org.assertj.core.api.Assertions.*;

public class NumberBaseballTest {
    @Test
    @DisplayName("입력의 스트링의 길이는 3이어야한다.")
    void test1() {
        String string = "123";
        Answer answer = new Answer(string);
        assertThat(answer.toString());
    }
    @Test
    @DisplayName("입력의 스트링의 길이가 3이 아니면 구성된 숫자의 길이가 3이 아님 예외 발생")
    void test2() {
        String str = "12";

        // when
        var exception = catchThrowable(() -> new Answer(str));

        // then 예외가 터졌으면 좋겠어요
        assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("게임 시작 후 랜덤 3자리 수 생성 후 중복확인")
    void test3() {
        SaveRandom num1 = new SaveRandom();
        num1.BenOverLapRandomNum();

        assertThat(num1.randomNum.size()==3).isTrue();
    }
}
