
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src

COPY ["brasil_burger/brasil_burger.csproj", "brasil_burger/"]


RUN dotnet restore "brasil_burger/brasil_burger.csproj"


COPY . .


WORKDIR "/src/brasil_burger"
RUN dotnet publish "brasil_burger.csproj" -c Release -o /app/publish


FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS final
WORKDIR /app
COPY --from=build /app/publish .


ENV ASPNETCORE_URLS=http://+:10000
EXPOSE 10000


ENTRYPOINT ["dotnet", "brasil_burger.dll"]