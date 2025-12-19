using Microsoft.AspNetCore.Mvc;
using brasil_burger.Models;
using System.Text.Json;

namespace brasil_burger.Controllers;

public class CartController : Controller
{
   
    public IActionResult Index()
    {
        var cart = GetCart();
        return View(cart);
    }

    [HttpPost]
    public IActionResult AddToCart(int id, string nom, decimal prix, string image, string type)
    {
        var cart = GetCart();
        var item = cart.FirstOrDefault(i => i.Id == id && i.Type == type);

        if (item == null) {
            cart.Add(new CartItem { Id = id, Nom = nom, Prix = prix, Image = image, Quantite = 1, Type = type });
        } else {
            item.Quantite++;
        }

        SaveCart(cart);
        return RedirectToAction("Index");
    }

    
    public IActionResult Remove(int id, string type)
    {
        var cart = GetCart();
        cart.RemoveAll(i => i.Id == id && i.Type == type);
        SaveCart(cart);
        return RedirectToAction("Index");
    }

    private List<CartItem> GetCart() {
        var json = HttpContext.Session.GetString("Cart");
        return json == null ? new List<CartItem>() : JsonSerializer.Deserialize<List<CartItem>>(json)!;
    }

    private void SaveCart(List<CartItem> cart) {
        HttpContext.Session.SetString("Cart", JsonSerializer.Serialize(cart));
    }
}