# Board's Adventure

## About

Board's Adventure is a Java-based multiplayer board game developed as a second-year Bachelor's (L2) project at Université Paris Cité inspired by Monopoly and Mario Party. Built from scratch using Java Swing, the game features a dynamic path-based board system, custom JSON map loading, an in-game economy, customizable player avatars, items, and card effects.

The objective is to accumulate as many crowns and properties as possible within a restricted number of turns to claim sovereignty over the realm.

*Note: As this was an academic project, the codebase and user interface are primarily in French.*

## Tech Stack

- **Language:** Java
- **GUI Framework:** Java Swing (custom rendering and layered panes)
- **Data Persistence:** JSON parsing for map layout and cell configurations
- **Build System:** Standard Java compilation

## Key Features

- **Main Menu & Configuration:** Full graphical navigation including game mode selection (FFA or 2v2 teams), turn limits, double dice, custom audio settings, play against real person or bot, and an interactive digital manual for help and explanation.
- **Dynamic Board & Map System:** 4 differents maps are rendered from background illustrations combined with a JSON-driven coordinate system, creating non-linear paths with intersections.
- **Diverse Cell Mechanics:** Includes specialized tiles such as Start bonuses, dynamic coin gains/losses, property acquisitions, random card events, shops, dangers, jails, and teleporters.
- **Items & Equipment:** Two tiers of items—starter items chosen during character creation (with cooldowns) and consumable shop items bought during the game to gain tactical advantages.
- **In-Game Console & HUD:** Real-time feedback console logging player actions, dynamic player rankings, gold balances, star counts, property ownership trackers, and a chat.

## Project Structure

```text
📁 boards-adventure
│
├── 📁 docs/                        
│
└── 📁 src/
    └── 📁 main/
        ├── 📁 java/                        # Java source files
        │   ├── 📁 controller/              
        │   │   └── 📁 resources 
        │   ├── 📁 game/
        │   ├── 📁 model/
        │   │   └── 📁 config/
        │   └── 📁 view/
        │       ├── 📁 game/
        │       └── 📁 menu/
        │
        └── 📁 resources/                   # Game assets (sprites, maps, fonts, audio)
            ├── 📁 audio
            │   ├── 📁 music
            │   └── 📁 sound
            ├── 📁 fonts
            ├── 📁 images
            │   ├── 📁 common
            │   ├── 📁 game
            │   └── 📁 menu
            └── 📁 maps
                ├── 📁 frostharbor
                ├── 📁 kingdom
                ├── 📁 spaceship
                └── 📁 villanova
```

## Getting Started

### Prerequisites

- Java Development Kit (JDK 11 or higher)

### Installation & Execution

1. Clone the repository:
```bash
git clone https://github.com/SuyuKuka/boards-adventure.git
cd boards-adventure
```

2. Launch the Game:  
    * Windows: Double-click the provided `run-game.bat` script.
    * Linux / macOS / Other Systems: Run the application directly via command line:
```bash
java -jar BoardsAdventure.jar
```

## Team

This project was developed collaboratively as 5-person team:

- **CHEN Michel** - Java Development & Game Architecture
  [![GitHub](https://img.shields.io/badge/GitHub-Profile-181717?logo=github)](https://github.com/SuyuKuka)
  [![LinkedIn](https://img.shields.io/badge/LinkedIn-Profile-0A66C2?logo=linkedin)](https://linkedin.com/in/chen-michel)

- **CHIV Henri** - Java Development & Game Architecture
  [![GitHub](https://img.shields.io/badge/GitHub-Profile-181717?logo=github)](https://github.com/H-flamme)

- **FADILI Ilias** - Java Development & Game Architecture
  [![GitHub](https://img.shields.io/badge/GitHub-Profile-181717?logo=github)](https://github.com/iliasfadili)

- **FROMENT Erwan** - Java Development & Game Architecture
  [![GitHub](https://img.shields.io/badge/GitHub-Profile-181717?logo=github)](https://github.com/fromente)

- **ROIG Lucas** - Java Development & Game Architecture
  [![GitHub](https://img.shields.io/badge/GitHub-Profile-181717?logo=github)](https://github.com/ll-tykas)
   
## License

This project was developed for academic purposes at Université Paris Cité. The source code is provided for portfolio demonstration purposes. 

*Note regarding assets:* Game assets (sprites, maps, audio, and textures) used in this repository belong to their respective copyright owners and are not distributed under an open-source license.