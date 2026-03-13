# AI Rules for this Application

## Tech Stack Overview

- **Frontend Framework**: React with TypeScript for type safety and component-based architecture
- **Routing**: React Router for navigation between pages
- **Styling**: Tailwind CSS for utility-first styling with responsive design
- **UI Components**: shadcn/ui library for pre-built, accessible components
- **Icons**: lucide-react package for consistent iconography
- **State Management**: React's built-in state management (useState, useContext) for simple cases, with potential for Redux or Zustand for complex state

## Library Usage Rules

- **Use shadcn/ui components** for standard UI elements like buttons, inputs, cards, modals, etc.
- **Use lucide-react icons** for all icon needs - no custom icons or other icon libraries
- **Use Tailwind CSS** for all styling - no CSS modules or styled-components
- **Use React Router** for navigation - no alternative routing solutions
- **Use TypeScript** for all components and files - no JavaScript
- **Use React's built-in hooks** for state management unless the app requires complex state that would benefit from a dedicated state management solution
- **Use only first-party imports** - no external libraries unless explicitly approved
- **Use the src/components directory** for all reusable components
- **Use the src/pages directory** for page-level components
- **Use the src/App.tsx** file for main routing configuration