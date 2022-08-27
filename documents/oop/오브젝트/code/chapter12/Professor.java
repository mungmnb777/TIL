package chapter12;

public class Professor {
    private String name;
    private Lecture lecture;

    public Professor(String name, Lecture lecture) {
        this.name = name;
        this.lecture = lecture;
    }

    /**
     * lecture를 가져와서 실제 통계를 계산하는 코드
     * Lecture에는 일반 Lecture와 GradeLecture 아무거나 와도 상관 없다.
     */
    public String compileStatistics() {
        return String.format("[%s] %s - Avg: %.1f", name,
                lecture.evaluate(), lecture.average());
    }
}