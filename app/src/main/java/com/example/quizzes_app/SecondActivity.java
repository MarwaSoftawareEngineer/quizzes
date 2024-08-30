package com.example.quizzes_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    TextView questionTextView;
    TextView totalQuestionTextView;
    Button ansA, ansB, ansC, ansD;
    Button btnSubmit;

    int score = 0;
    int totalQuestion = QuestionAnswer.question.length;
    int currentQuestionIndex = 0;
    String selectedAnswer = "";
    String[] userAnswers; // To store the user's answers
    StringBuilder resultSummary = new StringBuilder(); // To store the result summary

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        totalQuestionTextView = findViewById(R.id.total_question);
        questionTextView = findViewById(R.id.question);
        ansA = findViewById(R.id.ans_a);
        ansB = findViewById(R.id.ans_b);
        ansC = findViewById(R.id.ans_c);
        ansD = findViewById(R.id.ans_d);
        btnSubmit = findViewById(R.id.btn_submit);

        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        totalQuestionTextView.setText("Total questions: " + totalQuestion);

        // Initialize the array to store user answers
        userAnswers = new String[totalQuestion];

        loadNewQuestion();
    }

    private void loadNewQuestion() {
        if (currentQuestionIndex == totalQuestion) {
            finishQuiz();
            return;
        }

        // Display the question number with the question
        String questionText = "Question " + (currentQuestionIndex + 1) + ": " + QuestionAnswer.question[currentQuestionIndex];
        questionTextView.setText(questionText);

        ansA.setText(QuestionAnswer.choices[currentQuestionIndex][0]);
        ansB.setText(QuestionAnswer.choices[currentQuestionIndex][1]);
        ansC.setText(QuestionAnswer.choices[currentQuestionIndex][2]);
        ansD.setText(QuestionAnswer.choices[currentQuestionIndex][3]);

        selectedAnswer = "";

        // Reset the button backgrounds to the original style
        resetButtonStyles();
    }

    private void finishQuiz() {
        String passStatus = score >= totalQuestion * 0.6 ? "Passed" : "Failed";

        // Build the result summary with HTML formatting
        StringBuilder styledSummary = new StringBuilder();
        styledSummary.append("<h2>").append(passStatus).append("</h2>");
        styledSummary.append("<p><strong>Your Score: ").append(score).append(" out of ").append(totalQuestion).append("</strong></p>");
        styledSummary.append("<p>Here's a breakdown of your answers:</p>");
        styledSummary.append("<ul>");

        // Add each question and the user's answer
        for (int i = 0; i < totalQuestion; i++) {
            String userAnswer = userAnswers[i] != null ? userAnswers[i] : "No answer"; // Handle null values
            String correctAnswer = QuestionAnswer.correctAnswers[i]; // Get the correct answer for the current question

            styledSummary.append("<li>")
                    .append("<strong>Q").append(i + 1).append(":</strong> ").append(QuestionAnswer.question[i]).append("<br>")
                    .append("<strong>Your answer:</strong> ")
                    .append(userAnswer.equals(correctAnswer) ? "<font color='green'>" + userAnswer + "</font>" : "<font color='red'>" + userAnswer + "</font>")
                    .append("<br>")
                    .append("<strong>Correct answer:</strong> ").append(correctAnswer)
                    .append("</li><br>");
        }
        styledSummary.append("</ul>");

        // Display the result in an AlertDialog with custom styling
        new AlertDialog.Builder(this)
                .setTitle("Quiz Results")
                .setMessage(Html.fromHtml(styledSummary.toString(), Html.FROM_HTML_MODE_LEGACY))
                .setPositiveButton("Restart", (dialog, i) -> restartQuiz())
                .setCancelable(false)
                .show();
    }

    private void restartQuiz() {
        score = 0;
        currentQuestionIndex = 0;
        resultSummary.setLength(0); // Clear the previous result summary
        userAnswers = new String[totalQuestion]; // Reset the answers
        loadNewQuestion();
    }

    @Override
    public void onClick(View view) {
        Button clickedButton = (Button) view;

        if (clickedButton.getId() == R.id.btn_submit) {
            if (!selectedAnswer.isEmpty()) {
                // Save the selected answer
                userAnswers[currentQuestionIndex] = selectedAnswer;

                if (selectedAnswer.equals(QuestionAnswer.correctAnswers[currentQuestionIndex])) {
                    score++;
                }

                // Save the selected answer and whether it was correct
                resultSummary.append("Q").append(currentQuestionIndex + 1).append(": ")
                        .append(QuestionAnswer.question[currentQuestionIndex]).append("\n")
                        .append("Your answer: ").append(selectedAnswer).append("\n")
                        .append("Correct answer: ").append(QuestionAnswer.correctAnswers[currentQuestionIndex]).append("\n\n");

                currentQuestionIndex++;
                loadNewQuestion();
            }
        } else {
            selectedAnswer = clickedButton.getText().toString();
            resetButtonStyles();

            // Highlight the selected button
            clickedButton.setBackgroundColor(Color.parseColor("#FFC107")); // Yellow color for selected answer
        }
    }

    private void resetButtonStyles() {
        // Reset the button backgrounds to the original style
        ansA.setBackgroundResource(R.drawable.rounded_button);
        ansB.setBackgroundResource(R.drawable.rounded_button);
        ansC.setBackgroundResource(R.drawable.rounded_button);
        ansD.setBackgroundResource(R.drawable.rounded_button);
    }
}
