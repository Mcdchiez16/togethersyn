# TogetherSync — Duolingo-Inspired Couples Companion

A playful, emotionally engaging, and private Android companion app for couples to share communication logs, WhatsApp exports, milestones, and memories. Designed with a vibrant Duolingo-inspired aesthetic featuring tactile 3D buttons, bold solid colors (strictly no gradients), thick outlines, and an original lovebird mascot pair (Pip & Poko).

## User Review & Critical Decisions

> [!IMPORTANT]
> The following architectural and design decisions have been confirmed via Phase 1 clarification:
> - **Mascot Duo**: **Pip & Poko** — two cheerful, cartoon-style lovebirds who cheer on milestones, celebrate streaks, guide privacy settings, and react to shared memories.
> - **Partner Pairing & Sync Mode**: **Built-in partner switcher with live demo updates and real chat imports** — lets users seamlessly experience both perspectives (e.g., Alex & Sam), test real chat imports and updates immediately, and share invite pairing tokens.
> - **Permission Model**: **Dual Mode** — requests native runtime Android permissions (`READ_SMS`, `READ_CALL_LOG`) with full compliance and graceful handling, while offering curated, editable sample logs for immediate demonstration in emulator and sandbox environments.
> - **Zero-Gradient Rule**: 100% flat solid colors with dark solid bottom borders/shadows for depth and tactile feedback.

---

## 1. Overview & Core Concept

- **What It Does**: TogetherSync gives couples a dedicated, cozy private space to selectively share SMS logs, call logs, WhatsApp conversation exports, and relationship moments. It includes connection streaks, relationship milestones, celebratory animations, and fine-grained privacy controls (pause sharing, revoke logs, or delete items anytime).
- **Target Audience / Persona**: Couples in close or long-distance relationships who want playful, voluntary, and transparent connectivity without invasive background surveillance.
- **Key Value**: Gamified intimacy through delightful micro-interactions, streak badges, and milestone celebrations, while respecting privacy and explicit user consent at every step.

---

## 2. User Experience & Visual Design

### Key User Flows

1. **Onboarding Carousel (4 Steps)**:
   - Screen 1: *"Better Together! 💚"* (Pip & Poko cuddling on a branch; welcome to your shared space).
   - Screen 2: *"Connect Your Hearts 💕"* (Pairing code generation & invite flow).
   - Screen 3: *"Share Your Way 🔐"* (Clear consent policy; you choose what to share).
   - Screen 4: *"Let's Get Started! 🎉"* (Profile setup: Alex & Sam).
2. **Playful Home Dashboard**:
   - Greeting: *"Hey, Alex! 👋"* / *"Your little world together 💚"*.
   - Connected Avatars: Circular partner avatars linked by a pulsing animated heart.
   - Status & Streak Card: Connection status with current streak badge (e.g. "🔥 7-Day Streak!").
   - Feature Grid Cards: Messages 💬, Calls 📞, WhatsApp Shares 🟢, Our Moments 💜.
   - Partner Switcher Bar: Floating quick-toggle between Partner 1 (Alex) and Partner 2 (Sam).
3. **Messages & WhatsApp Import Flow**:
   - Filter by source (All, SMS, WhatsApp Shared).
   - WhatsApp Import: File picker or text paste dialog to import exported `.txt` chat logs, parsing sender, timestamp, and message bubbles.
   - Search bar with instant contact filtering.
4. **Call History Screen**:
   - Filter by Incoming (Green icon), Outgoing (Blue icon), and Missed (Coral icon).
   - Duration, timestamp, and contact cards with tactile elevation.
5. **Our Little World (Moments & Gamification)**:
   - Relationship milestone cards (First day, 7 days, 30 days, 1 year, First shared chat).
   - Memory notes builder (add notes, favorite moments, date nights).
   - Celebration Dialog: Confetti particles, stars, and lovebird cheer animations.
6. **Settings & Privacy Vault**:
   - Independent category toggles (Share Calls, Share SMS, Share WhatsApp, Share Notes).
   - "Pause Sharing" master switch and "Purge My Shared Data" action with confirmation.
   - Light/Dark theme toggle.

### Visual Identity & Theme Tokens

- **Aesthetic Direction**: Playful, cartoonish, tactile, and bold — inspired by Duolingo's flat 3D button interactions, chunky borders, and joyful mascot expressions.
- **Color Palette (Strictly Flat Solids — NO GRADIENTS)**:
  - Primary Green: `#58CC02` | Dark Green (Shadow): `#46A302`
  - Bright Blue: `#1CB0F6` | Dark Blue (Shadow): `#1899D6`
  - Yellow: `#FFC800` | Dark Yellow (Shadow): `#E5A500`
  - Purple: `#CE82FF` | Dark Purple (Shadow): `#BA68EE`
  - Coral: `#FF7777` | Dark Coral (Shadow): `#E05858`
  - Neutral Light Background: `#F7F9F6` | Card Surface: `#FFFFFF` | Border: `#E5E5E5`
  - Dark Background: `#131F24` | Dark Card Surface: `#202F36` | Dark Border: `#2E4048`
  - Main Text: `#3C3C3C` (Light) / `#F5F5F5` (Dark) | Secondary Text: `#777777` (Light) / `#A3B1B8` (Dark)
- **Typography & Touch Targets**:
  - Expressive bold display headings (24–28sp bold/extra-bold).
  - Friendly body typography (14–16sp medium).
  - Minimum 48dp touch targets on all interactive elements.

---

## 3. Key Product Decisions & Trade-Offs

- **Tactile 3D Button Pattern (`TogetherButton`)**:
  - *Chosen Approach*: Custom Compose component with a container `Box`, background shape with rounded corners (16dp), bottom shadow layer (4dp offset in darker shade), and spring press animation (`animateDpAsState` reducing offset on tap).
  - *Why*: Delivers the authentic Duolingo tactile feel without fragile canvas drawing or web CSS tricks.
- **Data Persistence (Room Database)**:
  - *Chosen Approach*: Room with SQLite for local persistence across app sessions, with pre-populated demo data and support for real WhatsApp chat text imports and runtime SMS/Call queries.
  - *Why*: Zero network latency, instant responsiveness, no sign-in barriers in the emulator, full privacy guarantee.
- **WhatsApp Import Engine**:
  - *Chosen Approach*: Regex-based Android `.txt` parser compatible with standard WhatsApp export formats (e.g. `[MM/DD/YY, HH:MM:SS] Name: message` and `MM/DD/YY, HH:MM - Name: message`).
  - *Why*: Allows users to import and view actual chats without unauthorized API scraping.

---

## 4. Technical Architecture & Data Strategy

### System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                       TogetherSync UI                           │
├─────────────────────────────────────────────────────────────────┤
│ [Onboarding]  [Home Dashboard]  [Messages]  [Calls]  [OurWorld] │
│      │               │               │         │         │      │
│      └───────┬───────┴───────┬───────┴─────────┴─────────┘      │
│              ▼               ▼                                  │
│   Together Design System Components                             │
│   ├── TogetherButton (3D tactile press)                         │
│   ├── TogetherCard (thick outline & rounded)                    │
│   ├── TogetherAvatar (Pip & Poko lovebirds)                     │
│   ├── TogetherBottomNavigation (5 tabs with pill)               │
│   ├── TogetherProgressBar (chunky solid bar)                    │
│   ├── TogetherAchievementBadge                                  │
│   ├── TogetherEmptyState                                        │
│   └── TogetherCelebrationDialog (confetti & stars)              │
├─────────────────────────────────────────────────────────────────┤
│                     TogetherSyncViewModel                       │
│   ├── Active Profile State (Alex vs Sam)                        │
│   ├── Connection & Streak State                                 │
│   ├── Message & Call Feed (Flow)                                │
│   ├── WhatsApp Import Handler                                   │
│   ├── Moments & Milestones State                                │
│   └── Privacy & Consent Controls State                          │
├─────────────────────────────────────────────────────────────────┤
│                   Room Database & Repository                    │
│   ├── MessageDao / MessageEntity (SMS & WhatsApp)               │
│   ├── CallLogDao / CallEntity                                   │
│   ├── MomentDao / MomentEntity                                  │
│   └── PartnerProfileDao / Settings                              │
└─────────────────────────────────────────────────────────────────┘
```

### Component & State Mapping

- `TogetherButton`: Props: `text`, `onClick`, `colorVariant` (Green, Blue, Yellow, Purple, Coral), `sizeVariant` (Primary, Secondary outlined), `icon`. Emits tactile press with animated elevation offset.
- `TogetherAvatar`: Renders cartoon character avatars (Pip in bright green with crest feather, Poko in soft coral with heart feather, or user photo).
- `TogetherBottomNavigation`: 5 tabs with rounded pill indicator, solid background tint, and bounce transition.
- `TogetherCelebrationDialog`: Pop-up modal with animated confetti particles, victory banner, and lovebirds dancing.
- `WhatsAppParser`: Utility parsing multi-line exported text into structured `MessageEntity` records tagged with source `WHATSAPP`.

---

## 5. Step-by-Step Implementation Sequence

1. **Metadata & Strings Setup**:
   - Update `metadata.json` (`name: "TogetherSync"`).
   - Configure `strings.xml` with all user-facing copy and titles.
2. **Color, Theme & Design Tokens**:
   - Update `Color.kt` and `Theme.kt` with solid colors, dark/light palettes, and shape definitions.
3. **Design System Components (`com.example.ui.components`)**:
   - `TogetherButton`: 3D tactile button with shadow elevation and click sound/haptic feedback.
   - `TogetherCard`: Rounded card with solid borders.
   - `TogetherAvatar`: Pip & Poko vector illustrations and avatar badges.
   - `TogetherBottomNav`: 5-tab bar with active indicator.
   - `TogetherAchievementBadge`: Milestone badges with stars.
   - `TogetherCelebrationDialog`: Animated celebration modal.
4. **Data Layer (`com.example.data`)**:
   - Entities: `MessageRecord`, `CallRecord`, `MomentRecord`, `PartnerProfile`.
   - DAOs: `TogetherDao` with Flow queries and insert/delete functions.
   - `AppDatabase`: Room database instance with initial seed data.
   - `WhatsAppParser`: Text parser for WhatsApp chat logs.
5. **ViewModel Layer (`com.example.ui.viewmodel`)**:
   - `TogetherViewModel`: Exposes reactive UI states, profile switcher, chat import, and privacy toggles.
6. **Screens (`com.example.ui.screens`)**:
   - `OnboardingScreen`: 4-step interactive intro.
   - `HomeScreen`: Dashboard with lovebirds, connection status, streak badge, and feature cards.
   - `MessagesScreen`: SMS + WhatsApp list, search, source filter, and chat viewer.
   - `CallsScreen`: Call history with duration and direction-colored icons.
   - `OurWorldScreen`: Shared memories, milestone tracker, and celebration triggers.
   - `SettingsScreen`: Privacy toggles, partner switching, and sharing revocation.
7. **Verification**:
   - Compile using `compile_applet` and verify zero errors and smooth navigation.
