// Matchers extras (toBeInTheDocument, toHaveStyle...) + limpeza automática do DOM.
import "@testing-library/jest-dom/vitest";
import { cleanup } from "@testing-library/react";
import { afterEach } from "vitest";

afterEach(() => cleanup());
