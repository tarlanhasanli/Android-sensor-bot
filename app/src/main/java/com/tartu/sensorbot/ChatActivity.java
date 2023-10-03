package com.tartu.sensorbot;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.tartu.sensorbot.activityHandlers.ChatRecyclerViewHandler;
import com.tartu.sensorbot.bot.BotResponseGenerator;
import com.tartu.sensorbot.logger.LoggerPermissionUtil;
import com.tartu.sensorbot.message.Message;
import com.tartu.sensorbot.message.MessageAdapter;
import java.io.IOException;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

  private static final String CONDITION_INDENT_KEY = "condition";
  private EditText inputEditText;
  private MessageAdapter messageAdapter;
  private BotResponseGenerator responseGenerator;
  private String condition;

  private LoggerPermissionUtil loggerPermissionUtil;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    inputEditText = findViewById(R.id.inputEditText);
    this.condition = getIntent().getStringExtra(CONDITION_INDENT_KEY);
    this.messageAdapter = new MessageAdapter(condition);

    try {
      responseGenerator = new BotResponseGenerator(condition, this);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    setUpChatRecyclerView();
    handleSendButtonClickListener();

    loggerPermissionUtil = new LoggerPermissionUtil(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    loggerPermissionUtil.checkPermissionsAndSettings();
  }

  private void setUpChatRecyclerView() {
    View rootView = findViewById(android.R.id.content);
    ChatRecyclerViewHandler handler = new ChatRecyclerViewHandler(rootView, this, condition);
    handler.initialize();
  }

  private void handleSendButtonClickListener() {
    Button sendButton = findViewById(R.id.sendButton);
    sendButton.setOnClickListener(v -> onSendButtonClick());
  }

  private void onSendButtonClick() {
    String userMessage = inputEditText.getText().toString().trim();
    if (!userMessage.isEmpty()) {
      messageAdapter.addMessage(userMessage, false);
      inputEditText.setText("");
      simulateBotResponse(userMessage);
    }
  }

  private void simulateBotResponse(String userQuery) {
    new Handler().postDelayed(() -> {
      List<Message> responses = responseGenerator.generateResponse(userQuery);
      messageAdapter.addBotMessages(responses);
    }, 2000);
  }
}
