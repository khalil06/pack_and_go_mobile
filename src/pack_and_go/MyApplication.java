package pack_and_go;



import static com.codename1.ui.CN.*;

import com.codename1.components.SpanLabel;
import com.codename1.io.rest.Response;
import com.codename1.io.rest.Rest;
import com.codename1.ui.*;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.io.Log;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.codename1.ui.layouts.BoxLayout;
import pack_and_go.Entities.Personality;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename One</a> for the purpose 
 * of building native mobile applications using Java.
 */
public class MyApplication {
    public static int sum(int[] intArrays) {
        int sum = 0;
        for (int number : intArrays)
            sum += number;
        return sum;
    }
    private Form current;
    private Resources theme;
    public static String  finalAnswer="";
    public static List<Personality> personalities;

    public void init(Object context) {
        // use two network threads instead of one
        updateNetworkThreadCount(2);

        theme = UIManager.initFirstTheme("/theme");

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature
        Log.bindCrashProtection(true);

        addNetworkErrorListener(err -> {
            // prevent the event from propagating
            err.consume();
            if(err.getError() != null) {
                Log.e(err.getError());
            }
            Log.sendLogAsync();
            Dialog.show("Connection Error", "There was a networking error in the connection to " + err.getConnectionRequest().getUrl(), "OK", null);
        });        
    }
    
    public void start() {
        if(current != null){
            current.show();
            return;
        }
        //Form hi = new Form("JSON Parsing", new BoxLayout(BoxLayout.Y_AXIS));

//        hi.show();
        getData();

    }
    public void getData(){
        Form hi = new Form("JSON Parsing", new BoxLayout(BoxLayout.Y_AXIS));

        Response<Map> jsonData = Rest.
                get("http://127.0.0.1:8000/mobile").
                acceptJson().
                getAsJsonMap();
        List<Map<String, Object>> res= (List<Map<String, Object>>) jsonData.getResponseData().get("root");
/*        for(Map<String, Object> obj : res) {
            String personalityId = (String)obj.get("personalityId");
            Object decisionMaking=(Object)obj.get("decisionMaking");
            Object interaction = (Object)obj.get("interaction");
            Object processing=(Object)obj.get("processing");
            Object social=(Object)obj.get("social");
            System.out.println(personalityId);
            System.out.println(decisionMaking);
            System.out.println(interaction);
            System.out.println(processing);
            System.out.println(social);
        }*/

       personalities = res.stream().map(obj->{

           String personalityId = (String)obj.get("personalityId");
           LinkedHashMap decisionMaking=(LinkedHashMap)obj.get("decisionMaking");
           LinkedHashMap interaction = (LinkedHashMap)obj.get("interaction");
           LinkedHashMap processing=(LinkedHashMap)obj.get("processing");
           LinkedHashMap social=(LinkedHashMap)obj.get("social");
                    return new Personality(personalityId,social,processing,decisionMaking,interaction);
                }).collect(Collectors.toList());
        int questionNb = 1;
        int[] extrovertVsIntrovertAnswersStorage = {2, 2, 2, 2, 2};//new int[5];// answer storing
        String[] extroversionVsIntroversionTest = {
                "A. expend energy, enjoy groups. B. conserve energy, enjoy one-on-one :",
                "A. more outgoing, think out loud. B. more reserved, think to yourself :",
                "A. seek many tasks, public activities, interaction with others. :" +
                        "B. seek private solitary activities with quiet to concentrate :",
                "A. external, communicative,  express yourself. B. internal, reticent, keep to yourself :",
                "A. active, initiate. B. reflective, deliberate :"};
        for (String question:extroversionVsIntroversionTest
             ) {
            Label questionNumber = new Label("Question Number : " + questionNb);
            int finalQuestionNb = questionNb - 1;

            Label questionLabel =new Label(question);
            RadioButton A = new RadioButton("A");
            A.addActionListener(evt -> extrovertVsIntrovertAnswersStorage[finalQuestionNb] = 1);
            RadioButton B= new RadioButton("B");
            B.addActionListener(evt -> extrovertVsIntrovertAnswersStorage[finalQuestionNb] = 0);
            new ButtonGroup(A, B);
            hi.add(questionLabel);
            hi.add(A);
            hi.add(B);
            questionNb++;
        }
        Button next= new Button("Next");
        next.addActionListener(evt -> {
            int sumOfAsInExtroversion= sum(extrovertVsIntrovertAnswersStorage);
            // append personality type accordingly
            System.out.println(" I OR E "+sumOfAsInExtroversion);
            if (sumOfAsInExtroversion < 3)
                finalAnswer=finalAnswer+"I";
            else {
                finalAnswer=finalAnswer+"E";
            }
            System.out.println("final answer"+finalAnswer);
            page2();
        });
    //    next.addActionListener();
        hi.add(next);
        hi.show();
    }
    public void page2(){
        Form q2 = new Form("JSON Parsing", new BoxLayout(BoxLayout.Y_AXIS));
        int questionNb = 1;
          int[] sensingVsIntuitionsAnswersStorage =  {2, 2, 2, 2, 2};// answer storing
        // sensing vs intuition questions
         String[] sensingVsIntuitionTest = {
                "A. interpret literally. B. look for meaning and possibilities",
                "A. practical, realistic, experiential. B. imaginative, innovative, theoretical",
                "A. standard, usual, conventional. B. different, novel, unique",
                "A. focus on here-and-now\" .B.look to the future, global perspective, \"big picture\"",
                "A. facts, things, \"what is\". B. ideas, dreams, \"what could be,\" philosophical"
        };
        for (String question:sensingVsIntuitionTest
        ) {
            Label questionNumber = new Label("Question Number : " + questionNb);

            Label questionLabel =new Label(question);
            int finalQuestionNb = questionNb - 1;
            RadioButton A = new RadioButton("A");
            A.addActionListener(evt -> sensingVsIntuitionsAnswersStorage[finalQuestionNb] = 1);
            RadioButton B= new RadioButton("B");
            B.addActionListener(evt -> sensingVsIntuitionsAnswersStorage[finalQuestionNb] = 0);
            new ButtonGroup(A, B);
            q2.add(questionNumber);
            q2.add(questionLabel);
            q2.add(A);
            q2.add(B);
            questionNb++;
        }
        Button next= new Button("Next");
        next.addActionListener(evt -> {
            int sumOfAsInSensing= sum(sensingVsIntuitionsAnswersStorage);
            // append personality type accordingly
            if (sumOfAsInSensing < 3)
                finalAnswer=finalAnswer+"N";
            else {
                finalAnswer=finalAnswer+"S";
            }
            page3();
        });
        q2.add(next);
        q2.show();
    }
    public void page3(){
        Form q3 = new Form("JSON Parsing", new BoxLayout(BoxLayout.Y_AXIS));
        String answer;
        int questionNb = 1;
        int[] thinkingVsFeelingAnswersStorage =  {2, 2, 2, 2, 2};// answer storing
        // sensing vs intuition questions
        String[] thinkingVsFeelingTest = {
                "A. logical, thinking, questioning. B. empathetic, feeling, accommodating",
                "B. candid, straight forward, frank. B.tactful, kind, encouraging",
                "A. firm, tend to criticize, hold the line. B. gentle, tend to appreciate, conciliate",
                "A. tough-minded, just B.tender-hearted, merciful",
                "A. matter of fact, issue-oriented B. sensitive, people-oriented, compassionate",
        };
        for (String question:thinkingVsFeelingTest
        ) {
            int finalQuestionNb = questionNb - 1;
            Label questionNumber = new Label("Question Number : " + questionNb);

            Label questionLabel =new Label(question);
            RadioButton A = new RadioButton("A");
            A.addActionListener(evt -> thinkingVsFeelingAnswersStorage[finalQuestionNb] = 1);
            RadioButton B= new RadioButton("B");
            B.addActionListener(evt -> thinkingVsFeelingAnswersStorage[finalQuestionNb] = 0);
            new ButtonGroup(A, B);

            q3.add(questionNumber);
            q3.add(questionLabel);
            q3.add(A);
            q3.add(B);
            questionNb++;

        }
        Button next= new Button("Next");
        next.addActionListener(evt -> {
            int sumOfAsInThinking= sum(thinkingVsFeelingAnswersStorage);
            // append personality type accordingly
            if (sumOfAsInThinking < 3)
                finalAnswer=finalAnswer+"F";
            else {
                finalAnswer=finalAnswer+"T";
            }
            page4();
        });
        //    next.addActionListener();
        q3.add(next);
        q3.show();
    }
    public void page4(){
        Form q4 = new Form("JSON Parsing", new BoxLayout(BoxLayout.Y_AXIS));
        String answer;
        int[] judgingVsPerceivingAnswersStorage =  {2, 2, 2, 2, 2};// answer storing
        // sensing vs intuition questions
         String[] judgingVsPerceivingTest = {
                "A. organized, orderly. B. flexible, adaptable",
                "A. plan, schedule B. unplanned, spontaneous",
                "A. regulated, structured B. easygoing, “live\" and “let live\"",
                "A. preparation, plan ahead. B. go with the flow, adapt as you go",
                "A. control, govern B. latitude, freedom"};
        for (String question:judgingVsPerceivingTest
        ) {

            Label questionLabel =new Label(question);
            RadioButton A = new RadioButton("A");
            RadioButton B= new RadioButton("B");
            new ButtonGroup(A, B);
            q4.add(questionLabel);
            q4.add(A);
            q4.add(B);
        }
        Button next= new Button("Next");
        next.addActionListener(evt -> {
            int sumOfAsInJudging= sum(judgingVsPerceivingAnswersStorage);
            // append personality type accordingly
            if (sumOfAsInJudging < 3)
                finalAnswer=finalAnswer+"P";
            else {
                finalAnswer=finalAnswer+"J";
            }
            result();
        });
        q4.add(next);
        q4.show();
    }
    public void result(){
try {
    Map<String, Object> jsonData = (Map<String, Object>) Rest.
            patch("http://127.0.0.1:8000/mobile/u/p").body(new Object() {
                public String personalityId =finalAnswer;
                public String userPersonalityId="3";

                @Override
                public String toString() {
                    return "$classname{" +
                            "personalityId='" + personalityId + '\'' +
                            ", userPersonalityId='" + userPersonalityId + '\'' +
                            '}';
                }
            }.toString()).acceptJson().getAsJsonMap();
}catch (Exception exception){
    System.out.println(exception);
}
              //  body().
                //acceptJson().
                //getAsJsonMap();
        List<Personality> filterPersonalities=personalities.stream().filter(personality -> personality.getPersonalityId().startsWith(finalAnswer)).collect(Collectors.toList());
        Form personalityReport = new Form("JSON Parsing", new BoxLayout(BoxLayout.Y_AXIS));
        filterPersonalities.forEach(filterPersonality->{
            Label personalityId=new Label(filterPersonality.getPersonalityId());
            SpanLabel sociacDetails=new SpanLabel(filterPersonality.getSocial().get("socialDetails").toString());
            SpanLabel processingDetails=new SpanLabel(filterPersonality.getProcessing().get("processingDetails").toString());
            SpanLabel interactionDetails=new SpanLabel(filterPersonality.getInteraction().get("interactionDetails").toString());
            SpanLabel thinkingDetails=new SpanLabel(filterPersonality.getDecisionMaking().get("decisionMakingDetails").toString());
            personalityReport.add(personalityId);
            personalityReport.add(sociacDetails);
            personalityReport.add(processingDetails);
            personalityReport.add(interactionDetails);
            personalityReport.add(thinkingDetails);
        });
        personalityReport.show();
    }


    public void stop() {
        current = getCurrentForm();
        if(current instanceof Dialog) {
            ((Dialog)current).dispose();
            current = getCurrentForm();
        }
    }
    
    public void destroy() {
    }

}
