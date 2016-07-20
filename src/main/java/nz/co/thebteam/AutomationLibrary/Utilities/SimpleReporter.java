package nz.co.thebteam.AutomationLibrary.Utilities;


import org.json.simple.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static nz.co.thebteam.AutomationLibrary.Utilities.FileHandler.writeOutFile;
import static nz.co.thebteam.AutomationLibrary.Utilities.TextPrettifier.indentJson;

public class SimpleReporter {
    // A very simple table creator that uses the json from a standard Cucumber run
    // Written because Confluence and the standard html output don't play nicely
    JSONParser jsonParser = null;
    List<JSONObject> allScenarios = null;
    int scenarioCount = 0;
    int scenarioFailedCount = 0;
    int scenarioPassedCount = 0;
    float totalTime = 0;
    String reportOutput = "";
    String reportTitle;
    String fileName = "";


    List<TestFeature> testFeatures = new ArrayList<>();

    public SimpleReporter(String json) {
        jsonParser = new JSONParser(json);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        DateFormat fileDateFormat = new SimpleDateFormat("dd_MM_yy_HH_mm_ss");

        Date date = new Date();
        fileName = fileDateFormat.format(date);
        reportTitle = "Report generated: " + dateFormat.format(date);
        parseTests();
        writeOutReport();

    }

    public SimpleReporter(String json, String title) {
        jsonParser = new JSONParser(json);
        DateFormat fileDateFormat = new SimpleDateFormat("dd_MM_yy_HH_mm_ss");

        Date date = new Date();
        fileName = fileDateFormat.format(date);
        reportTitle = title;
        parseTests();
        writeOutReport();

    }

    private void writeOutReport() {
        //styles
        reportOutput += "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "table {\n" +
                "    width:100%;\n" +
                "}\n" +
                "table#step {\n" +
                "    width:98%;\n" +
                "}\n" +
                "table, th, td {\n" +
                "    border: 1px solid black;\n" +
                "    border-collapse: collapse;\n" +
                "}\n" +
                "th, td {\n" +
                "    padding: 5px;\n" +
                "    text-align: left;\n" +
                "}\n" +
                "table tr:nth-child(even) {\n" +
                "    background-color: #eee;\n" +
                "}\n" +
                "table tr:nth-child(odd) {\n" +
                "   background-color:#fff;\n" +
                "}\n" +
                "table th {\n" +
                "    background-color: black;\n" +
                "    color: white;\n" +
                "}" +
                "</style>\n" +
                "</head>\n" +
                "<body>";

        List<String> ids = new ArrayList<>();

        //header
        //  reportOutput += "<a class=\"confluence-link\" href=\"#Testing-id\" data-mce-href=\"#Testing-id\" data-base-url=\"https://jiragenesisenergy.atlassian.net/wiki\" data-linked-resource-default-alias=\"id\" data-anchor=\"id\">Visit the Useful Tips Section</a>";
        reportOutput += "<h1>" + reportTitle + "</h1>";
        reportOutput += "<table style=\"width:80%\">\n";
        reportOutput += "<tr>\n";
        reportOutput += "    <th>Total Scenarios</th>\n" +
                "    <th>Passed</th> \n" +
                "    <th>Failed</th> \n" +
                "    <th>Total Time (seconds)</th>\n";
        reportOutput += "</tr>\n";
        reportOutput += "<tr>\n";
        reportOutput += "    <td>" + scenarioCount + "</td>\n" +
                "    <td id=\"tips\"=>" + scenarioPassedCount + "</td> \n" +
                "    <td>" + scenarioFailedCount + "</td> \n" +
                "    <td>" + String.format("%.2f", totalTime) + "</td>\n";

        reportOutput += "</tr>\n";
        reportOutput += "</table><br>";


        //failure quicklinks

        if (scenarioFailedCount > 0) {
            reportOutput += "<h2>Quicklinks to failed tests</h2>";

            int i = 1;
            reportOutput += "<table style=\"width:80%\">\n";
            reportOutput += "<tr>\n" +
                    "    <th>Scenario Name</th> \n";
            reportOutput += "    <th>Failed step</th>\n";
            reportOutput += "</tr>\n";


            for (TestFeature feature : testFeatures) {
                for (TestScenario scenario : feature.testScenarios) {
                    if (scenario.testResult.equals("failed")) {
                        String failedTag = "failed" + i;
                        ids.add(failedTag);
                        reportOutput += "<tr>\n" +
                                "    <td><a class=\"confluence-link\" href=\"#Testing-" + failedTag + "\" data-mce-href=\"#Testing-" + failedTag + "\" data-linked-resource-default-alias=\"" + failedTag + "\" data-anchor=\"" + failedTag + "\" >" + scenario.testName + "</a></td> \n";

                        for (TestStep step : scenario.steps) {
                            if (step.result.equals("failed")) {
                                reportOutput += "    <td>" + step.description + "</td>\n";
                                break;
                            }
                        }
                        reportOutput += "</tr>\n";

                    }
                }

            }

            reportOutput += "</table><br>";
        }


        for (TestFeature feature : testFeatures) {
            int i = 0;
            //Feature table - header
            reportOutput += "<table style=\"width:80%\">\n";
            reportOutput += "<tr>\n";
            reportOutput += "    <th style=\"width:20%\">Feature Name</th>\n" +
                    "    <th>Feature tags</th> \n";

            reportOutput += "</tr>\n";
            reportOutput += "<tr>\n";
            reportOutput += "    <td>" + feature.featureName + "</td>\n" +
                    "    <td>" + feature.featureTags + "</td> \n";
            reportOutput += "</tr>\n";


            //Scenarios
            for (TestScenario scenario : feature.testScenarios) {
                reportOutput += "<tr>\n";
                reportOutput += "    <th colspan=2>Scenario</th>\n";

                reportOutput += "</tr>\n";


                reportOutput += "<tr>\n";
                reportOutput += "    <td>Scenario Name</td>\n";

                if (scenario.testResult.equals("failed")) {
                    reportOutput += "    <td><p data-macro-default-parameter=\"" + ids.get(i) + "\"  class=\"editor-inline-macro\"  data-macro-name=\"anchor\">" + scenario.testName + "</p></td> \n";
                    i++;
                } else {
                    reportOutput += "    <td>" + scenario.testName + "</td> \n";
                }

                reportOutput += "</tr>\n";
                reportOutput += "<tr>\n";
                reportOutput += "    <td>Result</td>\n" +
                        "    <td>" + scenario.testResult + "</td> \n";
                reportOutput += "</tr>\n";

                if (scenario.tags != null) {
                    reportOutput += "<tr>\n";
                    reportOutput += "    <td>Tags</td>\n" +
                            "    <td>" + scenario.tags + "</td> \n";
                    reportOutput += "</tr>\n";
                }
                //embed the steps in a table within the table for emphasis as confluence strips away all formatting
                //Steps
                reportOutput += "<tr><td colspan=2>\n";
                for (TestStep step : scenario.steps) {
                    reportOutput += "<table id=\"step\">\n";
                    reportOutput += "<tr>\n";
                    reportOutput += "    <td width=\"20%\">Step Name</td>\n" +
                            "    <td>" + step.description;
                    if (step.dataRows.size() > 0) { //datatable used by step
                        reportOutput += "<table>\n";
                        for (String row : step.dataRows) { //for each data Table row
                            String[] values = row.split(",");
                            reportOutput += "<tr>\n";
                            for (String value : values) {
                                reportOutput += "    <td>" + value + "</td>\n";
                            }
                            reportOutput += "</tr>\n";
                        }
                        reportOutput += "</table>\n";
                    }
                    reportOutput += "</tr>\n"; //result
                    reportOutput += "<tr>\n";
                    reportOutput += "    <td>Step Result</td>\n" +
                            "    <td>" + step.result + "</td> \n";
                    reportOutput += "</tr>\n";

                    if (step.error != null) { //error message
                        reportOutput += "<tr>\n";
                        reportOutput += "    <th>Error message</th>\n" +
                                "    <td>" + step.error + "</td> \n";
                        reportOutput += "</tr>\n";
                    }
                    if (step.info != null) { //info written to step (pictures one day perhaps?)
                        reportOutput += "<tr>\n";
                        reportOutput += "    <td>Info</td>\n";
                        reportOutput += "    <td>";
                        reportOutput += "<table>";
                        for (String info : step.info) {

                            reportOutput += "<tr><td>" + info + "</td></tr> \n";

                        }
                        reportOutput += "</table>";
                        reportOutput += "</td> \n";
                        reportOutput += "</tr>\n";
                    }
                    reportOutput += "</table><br>"; //end of step
                }
                reportOutput += "</td></tr>\n";  //end of scenario

            }

            reportOutput += "</table><br>";
        }

        reportOutput += "</body>\n" +
                "</html>";

        try {
            writeOutFile(reportOutput, fileName + ".html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseTests() {
        for (int i = 0; i < jsonParser.size(); i++) { //for each feature
            TestFeature feature = new TestFeature();
            feature.featureName = jsonParser.getAtIndex(i).getValue("name");
            feature.featureTags = jsonParser.getAtIndex(i).getChildJSONArray("tags").findAllValues("name").toString();
            feature.testScenarios = buildScenarios(jsonParser.getAtIndex(i));
            testFeatures.add(feature);
        }


        System.out.println("\n\nScenarios: " + scenarioCount);
        System.out.println("Passed: " + scenarioPassedCount);
        System.out.println("Failed: " + scenarioFailedCount);
        System.out.println("Total Time: " + String.format("%.2f", totalTime) + "\n");
        for (TestFeature features : testFeatures) {
            System.out.println("\n\nFeature: " + features.featureName);
            for (TestScenario scen : features.testScenarios) {
                System.out.println("\nScenario: " + scen.testName);
                System.out.println("Result: " + scen.testResult);
                System.out.println("Tags: " + scen.tags);
                System.out.println("Time in S: " + String.format("%.2f", scen.testTotalTime));
                for (TestStep step : scen.steps) {
                    System.out.println(step.description + ": " + step.result);
                    System.out.println(step.info);
                    System.out.println(step.error);
                    for (String row : step.dataRows) {
                        System.out.println(row);
                    }
                }
            }
        }

    }

    private List<TestScenario> buildScenarios(JSONParser jp) {
        List<TestScenario> testScenarios = new ArrayList<>();
        List<JSONObject> obj = jp.findNodes("elements");

        for (JSONObject scenario : obj) {
            TestScenario ts = new TestScenario();
            scenarioCount++;
            JSONParser scenarioDetails = new JSONParser(scenario);

            //scenario name
            ts.testName = scenarioDetails.findValueAnyLevel("name");

            //statuses
            List<String> status = scenarioDetails.findAllValues("status");
            if (status.contains("failed")) {
                scenarioFailedCount++;
                ts.testResult = "failed";
            } else {
                scenarioPassedCount++;
                ts.testResult = "passed";
            }

            //total scenario time
            List<String> times = scenarioDetails.getChildJSONArray("steps").findAllValues("duration");
            for (String time : times) {
                ts.testTotalTime += Double.parseDouble(time);
            }
            ts.testTotalTime = ts.testTotalTime / 1000000000;
            totalTime += ts.testTotalTime;


            //tags
            try {
                List<JSONObject> tags = scenarioDetails.findNodes("tags");
                for (JSONObject tagNode : tags) {
                    JSONParser parseTagNode = new JSONParser(tagNode);
                    ts.tags += parseTagNode.getValue("name");
                }
            } catch (Exception e) { //no output steps
                ts.tags = null;
            }

            //steps
            ts.steps = buildStep(scenarioDetails);

            testScenarios.add(ts);
        }
        return testScenarios;
    }

    private List<TestStep> buildStep(JSONParser obj) {
        List<TestStep> testSteps = new ArrayList<>();
        List<JSONObject> steps = obj.findNodes("steps");
        for (JSONObject step : steps) {
            JSONParser parseStep = new JSONParser(step);
            TestStep ts = new TestStep();
            ts.description = parseStep.getValue("keyword") + "" + parseStep.getValue("name");
            ts.result = parseStep.getChildJSONObject("result").getValue("status");
            try {
                List<String> output = new ArrayList<>();
                for (int i = 0; i < parseStep.getChildJSONArray("output").jsonArray.size(); i++) {
                    output.add(cleanJson((String) parseStep.getChildJSONArray("output").jsonArray.get(i)));
                }
                ts.info = output;

            } catch (Exception e) { //no output steps
                ts.info = null;
            }
            try {
                ts.error = parseStep.getChildJSONObject("result").getValue("error_message");
            } catch (Exception e) { //no errors
                ts.error = null;
            }

            try {
                ts.dataRows = clean(parseStep.getChildJSONArray("rows").findAllValues("cells"));
            } catch (Exception e) { //no data rows
            }


            testSteps.add(ts);
        }

        return testSteps;
    }

    private String cleanJson(String output) {
        String formatted = "";
        output = output.replace("\\u003d", "=");
        output = output.replace("\\u0026", "&");
        //split them
        if (output.contains("JSON Request:")) {
            String[] outputStrings = output.split("\nJSON Request: \n");
            String fixed = outputStrings[1].replace("\n", "<br>");
            fixed = fixed.replace(" ", "&nbsp;");
            formatted += "JSON Request:<br>" + fixed;
            return formatted;
        }
        if (output.contains("JSON Response:")) {
            String[] outputStrings = output.split("\nJSON Response: \n");
            String fixed = outputStrings[1].replace("\n", "<br>");
            fixed = fixed.replace(" ", "&nbsp;");
            formatted += "JSON Response:<br>" + fixed;
            return formatted;
        }
        return output;
    }

    private List<String> clean(List<String> allValues) {
        List<String> cleaned = new ArrayList<>();
        for (String value : allValues) {
            value = value.replace("[", "");
            value = value.replace("]", "");
            value = value.replace("\"", "");
            value = value.replace("\\/", "/");
            cleaned.add(value);
        }
        return cleaned;
    }


}

class TestScenario {
    String testName;
    String testResult;
    float testTotalTime = 0;
    String tags = null;
    List<TestStep> steps;

}

class TestFeature {
    String featureName;
    String featureTags;
    List<TestScenario> testScenarios = new ArrayList<>();
}

class TestStep {
    String description;
    String result;
    String error = null;
    List<String> info = null;
    List<String> dataRows = new ArrayList<>();


}
