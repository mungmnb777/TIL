package chapter12;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class GradeLecture extends Lecture {
    private final List<Grade> grades;

    public GradeLecture(String name, int pass, List<Grade> grades, List<Integer> scores) {
        super(name, pass, scores);
        this.grades = grades;
    }

    /**
     * GradeLecture의 evaluate는 super를 이용해 Lecture 클래스의 evaluate()를 먼저 실행한다.
     */
    @Override
    public String evaluate() {
        return super.evaluate() + ", " + gradesStatistics();
    }

    private String gradesStatistics() {
        return grades.stream().map(this::format).collect(joining(" "));
    }

    private String format(Grade grade) {
        return String.format("%s:%d", grade.getName(), gradeCount(grade));
    }

    private long gradeCount(Grade grade) {
        return getScores().stream().filter(grade::include).count();
    }

    /**
     * 메서드 오버로딩을 활용하여 메서드의 이름은 동일하지만 시그니처가 다른 메서드를 추가할 수도 있다.
     */
    public double average(String gradeName) {
        return grades.stream()
                .filter(each -> each.isName(gradeName))
                .findFirst()
                .map(this::gradeAverage)
                .orElse(0d);
    }

    /**
     * 자식 클래스에는 부모 클래스에는 없던 새로운 메서드를 추가하는 것도 가능하다.
     * 등급별 평균 성적 반환하는 메서드
     */
    private double gradeAverage(Grade grade) {
        return getScores().stream()
                .filter(grade::include)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }
}
