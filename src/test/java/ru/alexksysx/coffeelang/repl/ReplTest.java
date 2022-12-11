package ru.alexksysx.coffeelang.repl;

import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static org.testng.Assert.*;

public class ReplTest {

    @Test
    public void testFileRepl() {
        String input = "помол := эспрессо\n" +
                "температура := 95\n" +
                "давление := 9\n" +
                "вес := 16\n" +
                "вес_напитка := 32\n" +
                "помолоть_кофе(вес,помол)\n" +
                "выставить_давление(давление)\n" +
                "выставить_температуру(температура)\n" +
                "взять_двойной_холдер()\n" +
                "поставить_чашку(чашка_эспрессо)\n" +
                "вставить_холдер_в_группу()\n" +
                "готовить_кофе(вес_напитка)\n" +
                "ждать(0:45)\n" +
                "подать_напиток()";

        String expectedOutput = ">> Добавлена переменная помол со значением: эспрессо\r\n" +
                ">> Добавлена переменная температура со значением: 95\r\n" +
                ">> Добавлена переменная давление со значением: 9\r\n" +
                ">> Добавлена переменная вес со значением: 16\r\n" +
                ">> Добавлена переменная вес_напитка со значением: 32\r\n" +
                ">> Помолото 16,0 грамм кофе с уровнем помола эспрессо\r\n" +
                ">> На кофемашине выставлено давление 9,0 бар\r\n" +
                ">> На кофемашине выставлена температура 95,0 градусов\r\n" +
                ">> Для работы подготовлен двойной холдер\r\n" +
                ">> Под группу поставлена чашка для эспрессо\r\n" +
                ">> В группу вставлен двойной холдер\r\n" +
                ">> Через кофейную таблетку пролито 32,0 грамм воды с температурой 95,0 под давлением 9,0\r\n" +
                ">> Ожидаем 45 секунд\r\n" +
                ">> Кружка с кофе подана клиенту\r\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        Repl repl = new Repl();
        repl.startFile(outputStream, inputStream);
        assertTrue(outputStream.toString().equalsIgnoreCase(expectedOutput));
    }

}