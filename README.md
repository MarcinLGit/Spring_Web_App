# PAP2024L-Z14
Zespół:
- Tomasz Kurzela
- Marcin Łobacz
- Aleksandr Rogaczewski
- Szymon Stec

Opiekun:
- Kacper Kania

# Temat: Internetowy portal udostępniania zasobów

Aplikacja webowa, która pozwoli udostępniać własne pliki oraz przeszukiwać i pobierać pliki wstawione przez innych użytkowników.

# Wymagnia wstępne
1 Oczywistość interakcji (umiejscowienie elementów na stronie w intuicyjny sposób, np. logowanie umieszczone w widocznym miejscu, a nie ukryte na końcu strony), klarowność (zastosowanie atrakcyjnej kolorystyki i odpowiedniego stylu za pomocą CSS, co ułatwia nawigację użytkownikowi), innowacyjność (wykorzystanie interaktywnych funkcji na stronie, podpowiedzi w polach tekstowych, walidacja wprowadzanych danych oraz wyświetlanie informacyjnych komunikatów, np. po pomyślnym zalogowaniu), oraz jakość wykonania (poprawność działania aplikacji, obsługa błędów poprzez wyświetlanie spójnych stron zgodnych z całą stylistyką aplikacji, np. w przypadku próby usunięcia powiązanego rekordu, nieistniejącej strony, lub błędu wewnętrznego). 

 
2  Aplikacja powinna obsługiwać co najmniej dwie perspektywy użytkowników: 

- Perspektywa „wąskiej”: Użytkownik po zalogowaniu ma dostęp jedynie do danych dotyczących jego osoby. Jednakże, powinien mieć możliwość modyfikacji, edycji, usuwania oraz dodawania części swoich danych. Na przykład, użytkownik po zalogowaniu na swoje konto może edytować wybrane dane, takie jak adres zamieszkania czy numer konta bankowego. Dodatkowo, może wykonywać akcje związane z jego profilem, takie jak rezerwacja samochodu z wypożyczalni czy zakup biletu do kina. 

- Perspektywa „szerokiej”: Użytkownik na prawach administratora ma możliwość zarządzania danymi w sposób rozszerzony. Obejmuje to wprowadzanie i usuwanie danych, a także modyfikację już istniejących. Przykładowo, po zalogowaniu na konto z uprawnieniami administratora, użytkownik może usuwać konta innych użytkowników, zatwierdzać rezerwacje dokonane przez zwykłych użytkowników, dodawać nowe stanowiska pracy (np. kierownik zmiany) i przypisywać je wybranym użytkownikom. 

Ważne jest, aby strona logowania była bezpieczna i zapewniała odpowiednią autoryzację użytkowników. Oznacza to, że nie można po zalogowaniu na konto zwykłego użytkownika uzyskać dostępu do panelu administratora poprzez manipulację adresem URL. Mechanizmy zabezpieczające powinny uniemożliwiać takie działania i wyświetlać odpowiednie komunikaty, informując użytkownika o braku odpowiednich uprawnień. 

 
3 Aplikacja powinna wspierać podstawowe operacje komunikacji z bazą danych, obejmujące: 
- Dodawanie danych 
- Usuwanie danych 
- Modyfikację danych 
- Przeglądanie danych 

Dla perspektywy „szerokiej”, czyli użytkownika na prawach administratora, powinny być dostępne wszystkie operacje. Na przykład, administrator powinien mieć możliwość dodawania nowych danych, usuwania istniejących, modyfikacji istniejących danych oraz przeglądania wszystkich danych w bazie. 

Dla perspektywy „wąskiej”, czyli zwykłego użytkownika, oprócz możliwości przeglądania danych, powinna być dostępna co najmniej jedna z pozostałych operacji. Na przykład, użytkownik może mieć możliwość modyfikacji swoich danych osobowych, ale nie usuwania ani dodawania nowych danych w systemie. 

Ważne jest, aby aplikacja zapewniała odpowiednie zabezpieczenia i kontrole dostępu, aby użytkownicy mieli dostęp tylko do tych operacji, do których mają uprawnienia. Na przykład, użytkownik z perspektywy „wąskiej” nie powinien mieć możliwości wykonywania operacji, do których nie ma uprawnień, takich jak usuwanie danych innych użytkowników. 


4 Baza danych jest prosta ma tylko 2 tabeli wiele do wielu, które zawierają dane użytkownika oraz informacje o przypisanych do niego książek 

 
