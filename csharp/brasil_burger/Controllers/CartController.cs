using Microsoft.AspNetCore.Mvc;
using brasil_burger.Models;
using System.Text.Json;

namespace brasil_burger.Controllers;

public class CartController : Controller {
    public IActionResult Index() {
        var cart = GetCart();
        return View(cart);
    }

    [HttpPost]
    public IActionResult AddToCart(int id, string nom, decimal prix, string image, List<string> selectedComplements, decimal extraPrix) {
        var cart = GetCart();
        
        cart.Add(new CartItem {
            Id = id,
            Nom = nom,
            Prix = prix,
            Image = image,
            SelectedComplements = selectedComplements,
            PrixComplements = extraPrix
        });

        SaveCart(cart);
        return RedirectToAction("Index");
    }

    public IActionResult Remove(string uniqueId) {
        var cart = GetCart();
        cart.RemoveAll(x => x.UniqueId == uniqueId);
        SaveCart(cart);
        return RedirectToAction("Index");
    }

    private List<CartItem> GetCart() {
        var json = HttpContext.Session.GetString("Cart");
        return json == null ? new List<CartItem>() : JsonSerializer.Deserialize<List<CartItem>>(json)!;
    }

    private void SaveCart(List<CartItem> cart) => 
        HttpContext.Session.SetString("Cart", JsonSerializer.Serialize(cart));
}