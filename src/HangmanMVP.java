import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class HangmanMVP {

    public static void main(String[] args) {

        // ========================================
        // 1. ПОДГОТОВКА ИГРЫ
        // ========================================

        // Слово, которое игрок должен угадать
        String secretWord = "java";

        // Объект для чтения ввода пользователя
        Scanner scanner = new Scanner(System.in);

        // Для каждой буквы слова храним информацию:
        // false — буква ещё скрыта
        // true  — буква уже открыта
        boolean[] revealedPositions = new boolean[secretWord.length()];

        // Количество ошибок игрока
        int mistakeCount = 0;

        // коллекция для хранения уже введённых букв

        Set<Character> guessedLetters= new LinkedHashSet<>();


        // Показываем слово в начале игры (все буквы скрыты)
        displayWord(revealedPositions, secretWord);


        // ========================================
        // 2. ОСНОВНОЙ ИГРОВОЙ ЦИКЛ
        // ========================================

        // Продолжаем игру, пока в слове есть скрытые буквы
        while (hasHiddenLetters(revealedPositions)) {

            // ========================================
            // 2.1. ВВОД БУКВЫ
            // ========================================

            System.out.println("Введите букву: ");
            String input = scanner.nextLine();


            // ========================================
            // 2.2. ПРОВЕРКА ВВОДА
            // ========================================

            // Пользователь должен ввести ровно один символ
            if (input.length() == 1) {

                char guessedLetter = input.charAt(0);

                // Разрешаем только строчные английские буквы
                if (guessedLetter >= 'a' && guessedLetter <= 'z') {

                    // ========================================
                    // 2.3. ПРОВЕРКА ПОВТОРНОГО ВВОДА
                    // ========================================

                    if (guessedLetters.contains(guessedLetter)) {

                        System.out.println("Буква уже ранее вводилась");

                    } else {

                        // ========================================
                        // 2.4. СОХРАНЕНИЕ НОВОЙ БУКВЫ
                        // ========================================

                        // добавляем и запоминаем введённую букву
                        guessedLetters.add(guessedLetter);
                        // Теперь буква добавляется в LinkedHashSet, а повторные значения не сохраняются. Переменная guessedLetterCount нам больше не нужна.


                        // ========================================
                        // 2.5. ВЫВОД ВВЕДЕННЫХ БУКВ
                        // ========================================
                        displayGuessedLetters(guessedLetters);
                        // ========================================
                        // 2.6. ПОИСК БУКВЫ В СЛОВЕ
                        // ========================================

                        // Открываем все позиции, где встречается буква
                        // true  — буква найдена
                        // false — буквы в слове нет
                        boolean letterFound = revealMatchingLetters(
                                revealedPositions,
                                secretWord,
                                guessedLetter
                        );


                        // ========================================
                        // 2.7. ОБНОВЛЕНИЕ КОЛИЧЕСТВА ОШИБОК
                        // ========================================

                        mistakeCount = updateMistakeCount(
                                letterFound,
                                mistakeCount
                        );




                        // ========================================
                        // 2.8. ПРОВЕРКА ПОРАЖЕНИЯ
                        // ========================================

                        // После шестой ошибки игра заканчивается
                        if (mistakeCount == 6) {

                            System.out.println("Вы проиграли. Было загадано: " + secretWord);
                            break;
                        }


                        // ========================================
                        // 2.9. ОТОБРАЖЕНИЕ СЛОВА
                        // ========================================

                        // Показываем текущее состояние слова
                        displayWord(revealedPositions, secretWord);
                    }

                } else {

                    System.out.println("Вы ввели не букву");
                }

            } else {

                System.out.println("Введите ровно одну букву!");
            }


            // ========================================
            // 3. ПРОВЕРКА ПОБЕДЫ
            // ========================================

            // Если скрытых букв больше нет — игрок победил
            if (!hasHiddenLetters(revealedPositions)) {

                System.out.println("Вы победили!");
            }
        }

        scanner.close();
    }


    // ========================================
    // 3.1 ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // ========================================


    // МЕТОД 1. ОТОБРАЖЕНИЕ СЛОВА
    //
    // Показывает текущее состояние загаданного слова.
    //
    // Если позиция ещё закрыта — выводит '_'.
    // Если позиция открыта — выводит соответствующую букву.
    //
    // Ничего не возвращает (void).

    static void displayWord(boolean[] revealedPositions, String secretWord) {

        for (int i = 0; i < revealedPositions.length; i++) {

            if (!revealedPositions[i]) {

                System.out.print('_');

            } else {

                System.out.print(secretWord.charAt(i));
            }
        }

        System.out.println();

    }


    // МЕТОД 2. ПРОВЕРКА СКРЫТЫХ БУКВ
    //
    // Проверяет, остались ли в слове неоткрытые позиции.
    //
    // Возвращает:
    // true  — хотя бы одна буква ещё скрыта
    // false — все буквы открыты

    static boolean hasHiddenLetters(boolean[] revealedPositions) {

        for (int i = 0; i < revealedPositions.length; i++) {

            if (!revealedPositions[i]) {

                return true;
            }
        }

        return false;
    }


    // МЕТОД 3. ПОИСК И ОТКРЫТИЕ БУКВЫ
    //
    // Ищет введённую букву в загаданном слове.
    //
    // Если буква встречается, открывает ВСЕ её позиции
    // в массиве revealedPositions.
    //
    // Возвращает:
    // true  — буква найдена хотя бы один раз
    // false — такой буквы в слове нет

    static boolean revealMatchingLetters(
            boolean[] revealedPositions,
            String secretWord,
            char guessedLetter
    ) {

        boolean letterFound = false;

        for (int i = 0; i < secretWord.length(); i++) {

            if (secretWord.charAt(i) == guessedLetter) {

                revealedPositions[i] = true;
                letterFound = true;
            }
        }

        return letterFound;
    }


    // МЕТОД 4. ПОДСЧЁТ ОШИБОК
    //
    // Получает информацию о том, найдена ли буква.
    //
    // Если буква не найдена:
    // - увеличивает количество ошибок на 1;
    // - выводит сообщение об ошибке;
    // - показывает текущее количество ошибок.
    //
    // Возвращает обновлённое количество ошибок.

    static int updateMistakeCount(boolean letterFound, int mistakeCount) {

        if (!letterFound) {

            mistakeCount++;

            System.out.println("Буква не найдена!");
            System.out.println("Ошибок: " + mistakeCount);
        }

        return mistakeCount;
    }


    // МЕТОД 5. Отображение введённых букв
    //
    // выводи информацию о том, какие буквы были введены.
    //
    public static void displayGuessedLetters(Set<Character> guessedLetters) {
        System.out.print("Введённые буквы: ");

        for (char letter: guessedLetters){
            System.out.print(letter + " ");
        }

        System.out.println();

    }

}