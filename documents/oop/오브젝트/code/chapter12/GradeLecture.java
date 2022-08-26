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
}
