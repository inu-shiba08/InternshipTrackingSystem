import exceptions.InvalidSubException;
import model.Evaluation;
import model.Internship;
import model.ProgressReport;
import service.EvaluationService;
import service.ProgressService;

/**
 * Basic tests for Parikshit's Stage 2 progress/evaluation responsibilities.
 *
 * These tests intentionally use plain Java assertions so the current pom.xml
 * does not need an additional testing dependency.
 *
 * Run with:
 *   javac -d out $(find src tests -name "*.java")
 *   java -ea -cp out ProgressEvaluationServiceTest
 */
public class ProgressEvaluationServiceTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        run("valid progress submission", ProgressEvaluationServiceTest::validProgressSubmission);
        run("empty progress rejected", ProgressEvaluationServiceTest::emptyProgressRejected);
        run("missing internship rejected", ProgressEvaluationServiceTest::missingInternshipRejected);
        run("duplicate week rejected", ProgressEvaluationServiceTest::duplicateWeekRejected);
        run("valid evaluation", ProgressEvaluationServiceTest::validEvaluation);
        run("invalid score rejected", ProgressEvaluationServiceTest::invalidScoreRejected);
        run("missing report rejected", ProgressEvaluationServiceTest::missingReportRejected);
        run("empty remarks rejected", ProgressEvaluationServiceTest::emptyRemarksRejected);
        run("report becomes evaluated", ProgressEvaluationServiceTest::reportBecomesEvaluated);

        System.out.println();
        System.out.println("Tests passed: " + passed);
        System.out.println("Tests failed: " + failed);

        if (failed > 0) {
            throw new AssertionError("Some tests failed.");
        }
    }

    private static ProgressService createProgressService() throws Exception {
        ProgressService service = new ProgressService();
        service.registerInternship(new Internship(
                "INT001",
                "S001",
                "TechNova Solutions",
                "Software Intern",
                8,
                "ONGOING"
        ));
        return service;
    }

    private static void validProgressSubmission() throws Exception {
        ProgressService service = createProgressService();

        ProgressReport report = service.submitProgress(
                "INT001",
                1,
                "Built login page and studied project code"
        );

        assert report != null;
        assert report.getInternshipId().equals("INT001");
        assert report.getWeekNumber() == 1;
        assert report.getWorkDescription().equals(
                "Built login page and studied project code");
        assert report.getStatus().equals("SUBMITTED");
    }

    private static void emptyProgressRejected() throws Exception {
        ProgressService service = createProgressService();

        expectInvalid(() ->
                service.submitProgress("INT001", 1, "   "));
    }

    private static void missingInternshipRejected() throws Exception {
        ProgressService service = createProgressService();

        expectInvalid(() ->
                service.submitProgress("MISSING", 1, "Completed assigned work"));
    }

    private static void duplicateWeekRejected() throws Exception {
        ProgressService service = createProgressService();
        service.submitProgress("INT001", 1, "First weekly report");

        expectInvalid(() ->
                service.submitProgress("INT001", 1, "Second report for the same week"));
    }

    private static void validEvaluation() throws Exception {
        ProgressService progressService = createProgressService();
        ProgressReport report = progressService.submitProgress(
                "INT001", 1, "Built login page");

        EvaluationService evaluationService = new EvaluationService(progressService);

        Evaluation evaluation = evaluationService.evaluateProgress(
                report.getReportId(),
                "M001",
                8.0,
                "Good progress; improve documentation"
        );

        assert evaluation != null;
        assert evaluation.getReportId().equals(report.getReportId());
        assert evaluation.getMentorId().equals("M001");
        assert evaluation.getScore() == 8.0;
        assert evaluation.getRemarks().equals(
                "Good progress; improve documentation");
    }

    private static void invalidScoreRejected() throws Exception {
        ProgressService progressService = createProgressService();
        ProgressReport report = progressService.submitProgress(
                "INT001", 1, "Built login page");

        EvaluationService evaluationService = new EvaluationService(progressService);

        expectInvalid(() ->
                evaluationService.evaluateProgress(
                        report.getReportId(), "M001", 11.0, "Invalid score"));
    }

    private static void missingReportRejected() throws Exception {
        ProgressService progressService = createProgressService();
        EvaluationService evaluationService = new EvaluationService(progressService);

        expectInvalid(() ->
                evaluationService.evaluateProgress(
                        "MISSING", "M001", 8.0, "No such report"));
    }

    private static void emptyRemarksRejected() throws Exception {
        ProgressService progressService = createProgressService();
        ProgressReport report = progressService.submitProgress(
                "INT001", 1, "Built login page");

        EvaluationService evaluationService = new EvaluationService(progressService);

        expectInvalid(() ->
                evaluationService.evaluateProgress(
                        report.getReportId(), "M001", 8.0, " "));
    }

    private static void reportBecomesEvaluated() throws Exception {
        ProgressService progressService = createProgressService();
        ProgressReport report = progressService.submitProgress(
                "INT001", 1, "Built login page");

        EvaluationService evaluationService = new EvaluationService(progressService);
        evaluationService.evaluateProgress(
                report.getReportId(), "M001", 8.0, "Good work");

        assert report.getStatus().equals("EVALUATED");
    }

    private static void expectInvalid(ThrowingAction action) throws Exception {
        try {
            action.run();
            throw new AssertionError("Expected InvalidSubException.");
        } catch (InvalidSubException expected) {
            // Expected result.
        }
    }

    private static void run(String name, ThrowingAction action) {
        try {
            action.run();
            passed++;
            System.out.println("[PASS] " + name);
        } catch (Throwable ex) {
            failed++;
            System.out.println("[FAIL] " + name + " -> " + ex.getMessage());
        }
    }

    @FunctionalInterface
    private interface ThrowingAction {
        void run() throws Exception;
    }
}
