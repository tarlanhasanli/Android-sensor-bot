package com.tartu.sensorbot.bot;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import com.tartu.sensorbot.chat.ChatAction;
import com.tartu.sensorbot.message.CheckboxEnabler;
import com.tartu.sensorbot.message.Message;
import com.tartu.sensorbot.message.MessageStep;
import java.io.IOException;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class BotResponseGeneratorTest extends TestCase {

  private Context context;

  @Before
  public void setUp() {
    context = ApplicationProvider.getApplicationContext();
  }

  @Test
  public void testGenerateResponseWithReference() throws IOException {
    // Given
    String userQuery = "How to save energy";
    BotResponseGenerator generator = new BotResponseGenerator(context);

    // When
    List<Message> responses = generator.generateResponse(userQuery);

    // Then
    assertEquals(2, responses.size());

    // Check start message
    assertEquals(BotMessageTemplates.BOT_RESPONSE_START, responses.get(0).getText());

    // check message steps
    List<MessageStep> referenceSteps = List.of(
        new MessageStep(0,
            "1. Ensure Bluetooth is enabled on both the Android device and the target device",
            ChatAction.NONE, CheckboxEnabler.BLUETOOTH_ON),
        new MessageStep(0,
            "2. Find a target device (e.g. nearby laptop, phone).",
            ChatAction.NONE,
            CheckboxEnabler.BLUETOOTH_CONNECTED),
        new MessageStep(0,
            "3. Connect to the target device.",
            ChatAction.NONE,
            CheckboxEnabler.BLUETOOTH_CONNECTED)
    );

    List<MessageStep> messageSteps = List.of(
        new MessageStep(2, "Close the background apps", ChatAction.CLOSE_APPS,
            CheckboxEnabler.NONE),
        new MessageStep(2, "Activate battery saving mode", ChatAction.ACTIVATE_SAVING_MODE,
            CheckboxEnabler.NONE),
        new MessageStep(6, "Perform computational offloading",
            ChatAction.MIGRATE_COMPUTATION, referenceSteps)
    );
    assertEquals(new Message(messageSteps), responses.get(1));
  }

  @Test
  public void testGenerateResponseWithoutReference() throws IOException {
    // Given
    String userQuery = "how to save battery ";
    BotResponseGenerator generator = new BotResponseGenerator(context);

    // When
    List<Message> responses = generator.generateResponse(userQuery);

    // Then
    assertEquals(2, responses.size());

    // Check start message
    assertEquals(BotMessageTemplates.BOT_RESPONSE_START, responses.get(0).getText());

    // check message steps
    List<MessageStep> referenceSteps = List.of(
        new MessageStep(0,
            "1. Ensure Bluetooth is enabled on both the Android device and the target device",
            ChatAction.NONE, CheckboxEnabler.BLUETOOTH_ON),
        new MessageStep(0,
            "2. Find a target device (e.g. nearby laptop, phone).",
            ChatAction.NONE,
            CheckboxEnabler.BLUETOOTH_CONNECTED),
        new MessageStep(0,
            "3. Connect to the target device.",
            ChatAction.NONE,
            CheckboxEnabler.BLUETOOTH_CONNECTED)
    );

    List<MessageStep> messageSteps = List.of(
        new MessageStep(2, "Close the background apps", ChatAction.CLOSE_APPS,
            CheckboxEnabler.NONE),
        new MessageStep(2, "Activate battery saving mode", ChatAction.ACTIVATE_SAVING_MODE,
            CheckboxEnabler.NONE),
        new MessageStep(6, "Perform computational offloading",
            ChatAction.MIGRATE_COMPUTATION, referenceSteps)
    );
    assertEquals(new Message(messageSteps), responses.get(1));
  }
}
