package chapter12;

import java.util.Collections;
import java.util.List;

public class Lecture {

    // 이수 여부의 기준 점수
    private final int pass;

    // 과목명
    private final String title;

    // 학생들의 성적을 보관할 리스트
    private final List<Integer> scores;

    public Lecture(String title, int pass, List<Integer> scores) {
        this.title = title;
        this.pass = pass;
        this.scores = scores;
    }

    /**
     * 전체 학생들의 평균 성적
     */
    public double average() {
        return scores.stream().mapToInt(Integer::intValue).average().orElse(0);
    }

    /**
     * 전체 학생들의 성적을 반환
     */
    public List<Integer> getScores() {
        return Collections.unmodifiableList(scores);
    }

    /**
     * 강의를 이수한 학생의 수와 낙제한 학생의 수를 형식에 맞게 구성한 후 반환
     */
    public String evaluate() {
        return String.format("Pass:%d Fail:%d", passCount(), failCount());
    }

    private long passCount() {
        return scores.stream().filter(score -> score >= pass).count();
    }

    private long failCount() {
        return scores.size() - passCount();
    }
}
