package net.sourceforge.marathon.junit.textui;

import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import static net.sourceforge.marathon.junit.textui.MarathonTestResult.STATUS_ERROR;
import static net.sourceforge.marathon.junit.textui.MarathonTestResult.STATUS_FAILURE;
import static net.sourceforge.marathon.junit.textui.MarathonTestResult.STATUS_NONE;
import static net.sourceforge.marathon.junit.textui.MarathonTestResult.STATUS_PASS;

public class JUnitOutputter implements IOutputter {

    @Override
    public void output(Writer writer, Test testSuite, Map<Test, MarathonTestResult> testOutputMap) {
        try {
            writer.write("<?xml version=\"1.0\"  encoding=\"UTF-8\"?>\n");
            writer.write("<testsuites>\n");
            writeTestsuite("", writer, testSuite, testOutputMap);
            writer.write("</testsuites>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTestsuite(String indent, Writer writer, Test test, Map<Test, MarathonTestResult> testOutputMap) throws IOException {
        if (test instanceof TestSuite) {
            TestSuite suite = (TestSuite) test;
            writer.write("<testsuite name=\"" + suite.getName() + "\" " +
                    "errors=\"" + getErrors(testOutputMap) + "\" " +
                    "skipped=\"" + getSkipped(testOutputMap) + "\" " +
                    "tests=\"" + suite.testCount() + "\" " +
                    "failures=\"" + getFailures(testOutputMap) + "\" " +
                    "time=\"" + getTime(testOutputMap) + "\" " +
                    "timestamp=\"" + new Date().getTime() + "\">\n");

            Enumeration<Test> testsEnum = suite.tests();
            while (testsEnum.hasMoreElements()) {
                writeTestsuite(indent + "  ", writer, (Test) testsEnum.nextElement(), testOutputMap);
            }
            writer.write(indent + "</testsuite>\n");
        } else {
            MarathonTestResult result = (MarathonTestResult) testOutputMap.get(test);
            writeTestCase(indent, writer, result, test);
        }
    }

    private int getErrors(Map<Test, MarathonTestResult> testOutputMap) {
        return getStatusCount(testOutputMap, STATUS_ERROR);
    }

    private int getFailures(Map<Test, MarathonTestResult> testOutputMap) {
        return getStatusCount(testOutputMap, STATUS_FAILURE);
    }

    private int getSkipped(Map<Test, MarathonTestResult> testOutputMap) {
        return getStatusCount(testOutputMap, STATUS_NONE);
    }

    private int getStatusCount(Map<Test, MarathonTestResult> testOutputMap, int status) {
        int statusCount = 0;
        for (MarathonTestResult result : testOutputMap.values()) {
            if (result.getStatus() == status) {
                statusCount++;
            }
        }

        return statusCount;
    }

    private int getTime(Map<Test, MarathonTestResult> testOutputMap) {
        int time = 0;
        for (MarathonTestResult result : testOutputMap.values()) {
            time += result.getDuration();
        }
        return time;
    }


    private void writeTestCase(String indent, Writer writer, MarathonTestResult result, Test test) throws IOException {
        writer.write(indent + "<testcase classname=\"" + result.getTestName() + "\" " +
                "name=\"" + result.getTestName() + "\" " +
                "time=\"" + result.getDuration() + "\">\n");
        writeTestCaseResult(indent, writer, result, test);
        writer.write(indent + "</testcase>\n");
    }


    private void writeTestCaseResult(String indent, Writer writer, MarathonTestResult result, Test test) throws IOException {

        String testLinkResult = "";
        if (result != null) {
            int status = result.getStatus();

            switch (status) {
                case STATUS_PASS:
                    break;
                case STATUS_NONE:
                    testLinkResult = "<skipped/>";
                    break;
                case STATUS_FAILURE:
                    testLinkResult = "<failure message=\"" + result.getStatusDescription() + "\">" +
                            result.getThrowable().getLocalizedMessage() +
                            "</failure>";
                    break;
                case STATUS_ERROR:
                    testLinkResult = "<error message=\"" + result.getStatusDescription() + "\">" +
                            result.getThrowable().getLocalizedMessage() +
                            "</error>";
                    break;
            }
        }
        writer.write(indent + testLinkResult + "\n");
    }

}
