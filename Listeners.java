package com.tos.library;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;

public class Listeners implements ISuiteListener, ITestListener, IInvokedMethodListener {

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		String textMsg = "About to begin executing following method : " + returnMethodName(method.getTestMethod());
		Reporter.log(textMsg, true);
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		String textMsg = "Completed executing following method : " + returnMethodName(method.getTestMethod());
		Reporter.log(textMsg, true);
	}

	@Override
	public void onTestStart(ITestResult result) {
		System.out.println("The execution of the main test starts now");
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		printTestResults(result);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		printTestResults(result);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		printTestResults(result);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	}

	@Override
	public void onStart(ITestContext context) {
		Reporter.log("About to begin executing Test " + context.getName(), true);
	}

	@Override
	public void onFinish(ITestContext context) {
		Reporter.log("Completed executing test " + context.getName(), true);
	}

	@Override
	public void onStart(ISuite suite) {
		Reporter.log("About to begin executing Suite " + suite.getName(), true);
	}

	@Override
	public void onFinish(ISuite suite) {
		Reporter.log("About to end executing Suite " + suite.getName(), true);
	}

	private void printTestResults(ITestResult result) {
		Reporter.log("Test Method resides in " + result.getTestClass().getName(), true);

		if (result.getParameters().length != 0) {

			String params = null;

			for (Object parameter : result.getParameters()) {

				params += parameter.toString() + ",";
			}

			Reporter.log("Test Method had the following parameters : " + params, true);
		}

		String status = null;

		switch (result.getStatus()) {

		case ITestResult.SUCCESS:

			status = "Pass";

			break;

		case ITestResult.FAILURE:

			status = "Failed";

			break;

		case ITestResult.SKIP:

			status = "Skipped";

		}
		Reporter.log("Test Status: " + status, true);
	}

	private String returnMethodName(ITestNGMethod testMethod) {
		return testMethod.getRealClass().getSimpleName() + "." + testMethod.getMethodName();
	}

}
