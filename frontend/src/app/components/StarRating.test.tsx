import { describe, it, expect } from "vitest";
import { render } from "@testing-library/react";
import StarRating from "./StarRating";

// A largura do overlay preenchido = fill * 100%, onde fill = clamp(rating - i, 0, 1).
// Lê a largura inline de cada camada de preenchimento (a 2ª div de cada estrela).
function fillWidths(container: HTMLElement): string[] {
  const overlays = container.querySelectorAll<HTMLDivElement>(
    "div.overflow-hidden",
  );
  return Array.from(overlays).map((el) => el.style.width);
}

describe("StarRating", () => {
  it("renderiza sempre 5 estrelas", () => {
    const { container } = render(<StarRating rating={3} />);
    expect(container.querySelectorAll("div.overflow-hidden")).toHaveLength(5);
  });

  it("nota inteira 3 preenche 3 estrelas cheias e 2 vazias", () => {
    const { container } = render(<StarRating rating={3} />);
    expect(fillWidths(container)).toEqual(["100%", "100%", "100%", "0%", "0%"]);
  });

  it("meia estrela: 3.5 preenche 3 cheias e 1 pela metade", () => {
    const { container } = render(<StarRating rating={3.5} />);
    expect(fillWidths(container)).toEqual(["100%", "100%", "100%", "50%", "0%"]);
  });

  it("clampa nota acima de 5 e abaixo de 0", () => {
    expect(fillWidths(render(<StarRating rating={9} />).container)).toEqual([
      "100%",
      "100%",
      "100%",
      "100%",
      "100%",
    ]);
    expect(fillWidths(render(<StarRating rating={-2} />).container)).toEqual([
      "0%",
      "0%",
      "0%",
      "0%",
      "0%",
    ]);
  });
});
