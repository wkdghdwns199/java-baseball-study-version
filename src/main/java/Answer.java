/**
 * 게임이 진행되는 동안 1~9로 구성된 서로 다른 3자리 수 입력
 * 게임 종료 후 1 혹은 2를 입력
 */
public class Answer {
    private int answer;

    public Answer(String inputString) {

        if (inputString.length() != 3) {
            throw new IllegalArgumentException("구성된 숫자의 길이가 3이 아님");
        }
        if (isDigit(inputString) == false) {
            throw new IllegalArgumentException("구성된 숫자의 길이가 3이 아님");
        }

        answer = Integer.parseInt(inputString);


    }

    private boolean isDigit(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return answer + "";
    }
}
