package com.atlantbh.jmeter.plugins.jtlparser;

import com.atlantbh.jmeter.plugins.jtlparser.builder.JunitXmlBuilder;
import com.atlantbh.jmeter.plugins.jtlparser.model.junit.TestCase;
import com.atlantbh.jmeter.plugins.jtlparser.model.junit.TestStep;
import org.apache.jmeter.JMeter;
import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.reporters.AbstractListenerElement;
import org.apache.jmeter.samplers.Remoteable;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestStateListener;
import com.atlantbh.jmeter.plugins.jtlparser.model.junit.*;
import org.apache.jmeter.timers.SyncTimer;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by adnan on 17/12/15.
 */

public class JtlParserLogger extends AbstractListenerElement
        implements SampleListener, Serializable,
        NoThreadClone, TestStateListener, Remoteable {

    private ArrayList<TestCase> testCases = new ArrayList<TestCase>();
    private PrintStream out;
    private String outputFile = "";
    public JtlParserLogger(){
        super();
    }

    public static JtlParserLogger getJtlParserLogger() {
        return new JtlParserLogger();
    }
    @Override
    public void sampleOccurred(SampleEvent sampleEvent) {
        TestCase testCase = getTestCase(sampleEvent);
        TestStep testStep = createTestStep(sampleEvent.getResult());
        testCase.addTestStep(testStep);
    }

    @Override
    public void sampleStarted(SampleEvent sampleEvent) {

    }

    @Override
    public void sampleStopped(SampleEvent sampleEvent) {

    }

    @Override
    public void testStarted() {
        if (JMeter.isNonGUI()) {
            out = System.out;
        }
        System.out.println("Object Started: " + this.toString());
    }

    @Override
    public void testStarted(String s) {
        testStarted();
    }

    @Override
    public void testEnded() {
        JunitXmlBuilder builder = JunitXmlBuilder.newInstance();
        ArrayList<TestSuite> testSuites = new ArrayList<TestSuite>();
        TestSuite testSuite = new TestSuite();
        for (TestCase testCase : testCases)
            testSuite.addTestCase(testCase);
        testSuites.add(testSuite);
        builder.writeXmlDoc(builder.generateXmlDoc(testSuites), outputFile);
        System.out.println("Object Ended: " + this.toString());
    }

    @Override
    public void testEnded(String s) {
        testEnded();
    }

    public void setOuputFile(String file) {
        outputFile = file;

    }

    public String getOutputFile() {
        return outputFile;
    }

    private TestStep createTestStep(SampleResult sampleResult){
        TestStep testStep = new TestStep();
        testStep.setName(sampleResult.getSampleLabel());
        testStep.setTime(String.valueOf(sampleResult.getTime()));

        for (AssertionResult assertionResult: sampleResult.getAssertionResults()) {
            if (assertionResult.isFailure() || assertionResult.isError()) {
                testStep.setAssertionFailures(assertionResult.getName(), assertionResult.getFailureMessage());
            }
        }

        return testStep;
    }

    private TestCase getTestCase(SampleEvent sampleEvent) {
        TestCase testCase = findTestCase(sampleEvent.getThreadGroup());
        if(testCase == null) {
            testCase = new TestCase();
            testCase.setClassName(sampleEvent.getThreadGroup());
            testCases.add(testCase);
        }
        return testCase;
    }

    private TestCase findTestCase(String testCaseName) {
        for(TestCase testCase: testCases) {
            if(testCase.getClassName().equals(testCaseName))
                return testCase;
        }
        return null;
    }
}
