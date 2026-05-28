# Chess

Desktopowa gra w szachy napisana w Javie z graficznym interfejsem użytkownika opartym na JavaFX. Projekt zrealizowany jako samodzielna aplikacja okienkowa z pełną logiką szachową i możliwością zapisu partii.

## Technologie

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=flat&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-GUI-blue?style=flat)

## Funkcjonalności

- Pełna logika gry w szachy (wszystkie figury, zasady ruchu)
- Walidacja legalności ruchów
- Specjalne ruchy: roszada, bicie w przelocie, promocja pionka
- Graficzna szachownica z animowanymi figurami
- Zapis i wczytywanie partii (pliki w katalogu `saves/`)
- Architektura MVC

## Struktura projektu

```
Chess/
├── src/
│   └── chess/        # Logika gry i kontrolery
├── resources/        # Grafiki figur
└── saves/            # Zapisane partie
```

## Uruchomienie

### Wymagania
- Java 17+
- JavaFX SDK

### Kroki

```bash
git clone https://github.com/Marvi217/Chess.git
cd Chess
```

Otwórz projekt w IntelliJ IDEA z dodanym modułem JavaFX, następnie uruchom klasę główną `Main`.
