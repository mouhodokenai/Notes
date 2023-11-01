package com.example.notes;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Activity2 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int num = bundle.getInt("id");

        Note currentNote;
        try (FileInputStream fileIn = openFileInput(num + "note.txt")) {
            ObjectInputStream in = new ObjectInputStream(fileIn);
            currentNote = (Note) in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        // Настройка полей для редактирования
        EditText name = findViewById(R.id.name);
        EditText text = findViewById(R.id.text);
        String currentName = currentNote.getName();
        String currentText = currentNote.getText();
        text.setText(currentText);
        name.setText(currentName);
        // Обработчики событий для полей редактирования
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setFocusableInTouchMode(true);
                name.setFocusable(true);
                name.setCursorVisible(true);
                name.requestFocus();
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setFocusableInTouchMode(true);
                text.setFocusable(true);
                text.setCursorVisible(true);
                text.requestFocus();
            }
        });

        Button saveButton = findViewById(R.id.saveButton);
        // Обработчик события для кнопки сохранения
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем отредактированные данные из полей ввода
                String editedTextname = name.getText().toString();
                String editedTexttext = text.getText().toString();
                // Обновляем данные в объекте Note
                currentNote.setName(editedTextname);
                currentNote.setText(editedTexttext);
                // Сохраняем обновленную заметку в файл
                try (ObjectOutputStream objOut = new ObjectOutputStream(openFileOutput(num + "note.txt", MODE_PRIVATE))) {
                    objOut.writeObject(currentNote);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("id", num); // Номер заметки, которую мы обновили
                resultIntent.putExtra("updatedNote", currentNote); // Обновленный объект Note

                // Устанавливаем результат и передаем Intent обратно в MainActivity
                setResult(Activity.RESULT_OK, resultIntent);

                // Завершаем Activity2
                finish();

            }
        });
    }

}