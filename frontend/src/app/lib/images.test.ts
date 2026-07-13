import { describe, it, expect } from "vitest";
import { fallbackImg } from "./images";

describe("fallbackImg", () => {
  it("retorna a url original quando presente", () => {
    expect(fallbackImg("https://x.com/foto.jpg", 1)).toBe("https://x.com/foto.jpg");
  });

  it("gera fallback deterministico do picsum quando url é null", () => {
    expect(fallbackImg(null, 42)).toBe("https://picsum.photos/seed/dishd-42/400");
  });

  it("gera fallback quando url é undefined", () => {
    expect(fallbackImg(undefined, "abc")).toBe(
      "https://picsum.photos/seed/dishd-abc/400",
    );
  });

  it("mesmo seed produz mesma url (estabilidade)", () => {
    expect(fallbackImg(null, 7)).toBe(fallbackImg(null, 7));
  });
});
