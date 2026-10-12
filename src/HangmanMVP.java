import java.util.Scanner;

public class HangmanMVP {
    public static void main(String[] args) {
        String secretWord = "java";
        Scanner scanner = new Scanner(System.in);
        boolean[] openedLetters = new boolean[secretWord.length()];
        showWord(openedLetters, secretWord);
        int countMistakes = 0;

        //Игровой цикл

        while (checkClosedLetters(openedLetters)) {
            System.out.println("Введите букву: ");
            String input = scanner.nextLine();
            if (input.length() == 1) {
                char guessLetter = input.charAt(0);
                if (guessLetter >= 'a' && guessLetter <= 'z') {
                    boolean foundLetter  = openedFindLetters(openedLetters, secretWord, guessLetter);
                    countMistakes = updateMistakeCount(foundLetter,countMistakes);
                    showWord(openedLetters,secretWord);
                } else {
                    System.out.println("Вы ввели не букву");
                }
            } else {
                System.out.println("Введите ровно одну букву!");
            }
        }
        System.out.println("вы победили!");
    }

    // Вывести текущее состояние слова
    static void showWord(boolean[] openedLetters, String secretWord) {
        for (int i = 0; i < openedLetters.length; i++) {
            if (!openedLetters[i]) {
                System.out.print('_');
            } else System.out.print(secretWord.charAt(i));
        }
        System.out.println();

    }

    static boolean checkClosedLetters(boolean[] openedLetters) {
        for (int i = 0; i < openedLetters.length; i++) {
            if (!openedLetters[i]) {
                return true;
            }
        }
        return false;
    }

    static boolean openedFindLetters(boolean[] openedLetters, String secretWord, char guessLetter){
        boolean letterFound = false;
        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == guessLetter) {
                openedLetters[i] = true;
                letterFound = true;
            }
        }
        return letterFound;
    }

    static int updateMistakeCount(boolean foundLetter, int count){
        if (!foundLetter){
            count = count + 1;
            System.out.println("буква не найдена!");
            System.out.println("Ошибок: " + count);
        }
        return count;
    }
}