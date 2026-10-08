# Tests

## Parikshit — Progress / Evaluation / Validation / Testing

`ProgressEvaluationServiceTest.java` covers:

- valid progress submission
- empty progress submission
- missing internship
- duplicate weekly submission
- valid mentor evaluation
- invalid score
- missing progress report
- empty evaluation remarks
- progress report status changing to `EVALUATED`

### Run

From the project root, compile the main `src` tree and the test:

```bash
mkdir out
javac -d out $(find src tests -name "*.java")
java -ea -cp out ProgressEvaluationServiceTest
```

On Windows PowerShell, an IDE can be used to compile/run the same classes, or the Java files can be passed explicitly to `javac`.

Expected result:

```text
Tests passed: 9
Tests failed: 0
```

## Integration note

`ProgressService` and `EvaluationService` currently keep records in memory because
`ProgressReportDAO` and `EvaluationDAO` are still TODOs. Once the DAO layer is
implemented, these services can be connected to those DAO methods without moving
business validation into the Swing UI.
